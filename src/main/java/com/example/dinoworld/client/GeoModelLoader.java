package com.example.dinoworld.client;

import com.example.dinoworld.DinoWorld;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds an entity model from a Bedrock geometry file ({@code assets/dinoworld/geo/<name>.geo.json}), the format
 * Blockbench's "Bedrock Entity" projects save, so models can be edited in Blockbench rather than in Java.
 * <p>
 * Bedrock geometry is y-up with the ground at y = 0, and gives bone pivots and cube origins in absolute
 * coordinates. Java models are y-down with the ground at y = 24, and give each part's pivot relative to its
 * parent and each cube relative to its part's pivot. The conversion matches Blockbench's Java entity exporter.
 * Bone names become part names, which is how {@code setupAnim} finds the parts it animates.
 * <p>
 * Java cubes can't be rotated on their own, so a rotated cube becomes a child part named
 * {@code <bone>_r<n>}. Only box UV is supported, as Java entity models have no per-face UV.
 */
public final class GeoModelLoader {
    private static final float GROUND_Y = 24.0f;

    private GeoModelLoader() {
    }

    public static LayerDefinition load(String name) {
        String file = "assets/" + DinoWorld.MOD_ID + "/geo/" + name + ".geo.json";
        Path path = FabricLoader.getInstance().getModContainer(DinoWorld.MOD_ID).orElseThrow()
                .findPath(file)
                .orElseThrow(() -> new IllegalStateException("Missing model file " + file));
        try (Reader reader = Files.newBufferedReader(path)) {
            return load(reader);
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Failed to load model file " + file + ": " + e.getMessage(), e);
        }
    }

    public static LayerDefinition load(Reader reader) {
        JsonArray geometries = JsonParser.parseReader(reader).getAsJsonObject().getAsJsonArray("minecraft:geometry");
        if (geometries == null || geometries.size() != 1) {
            throw new JsonParseException("expected exactly one entry in minecraft:geometry");
        }
        JsonObject geometry = geometries.get(0).getAsJsonObject();
        JsonObject description = geometry.getAsJsonObject("description");

        MeshDefinition mesh = new MeshDefinition();
        Map<String, PartDefinition> parts = new HashMap<>();
        Map<String, float[]> pivots = new HashMap<>();
        for (JsonElement element : geometry.getAsJsonArray("bones")) {
            JsonObject bone = element.getAsJsonObject();
            String name = bone.get("name").getAsString();
            if (parts.containsKey(name)) {
                throw new JsonParseException("duplicate bone " + name);
            }
            String parentName = bone.has("parent") ? bone.get("parent").getAsString() : null;
            PartDefinition parent = parentName == null ? mesh.getRoot() : parts.get(parentName);
            if (parent == null) {
                throw new JsonParseException("bone " + name + " has parent " + parentName + ", which isn't listed before it");
            }
            float[] parentPivot = parentName == null ? null : pivots.get(parentName);
            float[] pivot = floats(bone, "pivot");
            boolean mirror = bone.has("mirror") && bone.get("mirror").getAsBoolean();
            float inflate = bone.has("inflate") ? bone.get("inflate").getAsFloat() : 0.0f;

            CubeListBuilder cubes = CubeListBuilder.create();
            List<JsonObject> rotatedCubes = new ArrayList<>();
            if (bone.has("cubes")) {
                for (JsonElement cubeElement : bone.getAsJsonArray("cubes")) {
                    JsonObject cube = cubeElement.getAsJsonObject();
                    if (isZero(floats(cube, "rotation"))) {
                        addCube(cubes, cube, pivot, mirror, inflate);
                    } else {
                        rotatedCubes.add(cube);
                    }
                }
            }
            PartDefinition part = parent.addOrReplaceChild(name, cubes, pose(pivot, parentPivot, floats(bone, "rotation")));

            for (int i = 0; i < rotatedCubes.size(); i++) {
                JsonObject cube = rotatedCubes.get(i);
                float[] cubePivot = floats(cube, "pivot");
                part.addOrReplaceChild(name + "_r" + (i + 1),
                        addCube(CubeListBuilder.create(), cube, cubePivot, mirror, inflate),
                        pose(cubePivot, pivot, floats(cube, "rotation")));
            }
            parts.put(name, part);
            pivots.put(name, pivot);
        }

        return LayerDefinition.create(mesh, description.get("texture_width").getAsInt(), description.get("texture_height").getAsInt());
    }

    private static PartPose pose(float[] pivot, float[] parentPivot, float[] rotation) {
        float x = parentPivot == null ? pivot[0] : pivot[0] - parentPivot[0];
        float y = parentPivot == null ? GROUND_Y - pivot[1] : parentPivot[1] - pivot[1];
        float z = parentPivot == null ? pivot[2] : pivot[2] - parentPivot[2];
        return PartPose.offsetAndRotation(x, y, z,
                rotation[0] * Mth.DEG_TO_RAD, rotation[1] * Mth.DEG_TO_RAD, rotation[2] * Mth.DEG_TO_RAD);
    }

    private static CubeListBuilder addCube(CubeListBuilder builder, JsonObject cube, float[] pivot, boolean boneMirror, float boneInflate) {
        if (cube.has("uv") && !cube.get("uv").isJsonArray()) {
            throw new JsonParseException("per-face UV isn't supported; switch the Blockbench project to box UV");
        }
        float[] origin = floats(cube, "origin");
        float[] size = floats(cube, "size");
        float[] uv = floats(cube, "uv");
        boolean mirror = cube.has("mirror") ? cube.get("mirror").getAsBoolean() : boneMirror;
        float inflate = cube.has("inflate") ? cube.get("inflate").getAsFloat() : boneInflate;
        return builder.texOffs((int) uv[0], (int) uv[1]).mirror(mirror).addBox(
                origin[0] - pivot[0], pivot[1] - origin[1] - size[1], origin[2] - pivot[2],
                size[0], size[1], size[2], new CubeDeformation(inflate));
    }

    /** Reads a numeric array, defaulting missing entries to 0. */
    private static float[] floats(JsonObject object, String key) {
        float[] values = new float[3];
        if (object.has(key)) {
            JsonArray array = object.getAsJsonArray(key);
            for (int i = 0; i < array.size() && i < values.length; i++) {
                values[i] = array.get(i).getAsFloat();
            }
        }
        return values;
    }

    private static boolean isZero(float[] values) {
        return values[0] == 0.0f && values[1] == 0.0f && values[2] == 0.0f;
    }
}
