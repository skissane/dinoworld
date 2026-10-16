package com.example.dinoworld.client;

import com.google.gson.JsonParseException;
import net.minecraft.client.model.geom.ModelPart;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeoModelLoaderTest {
    @Test
    void triceratopsHasThePartsTheModelAnimates() throws Exception {
        try (Reader reader = new InputStreamReader(Objects.requireNonNull(
                GeoModelLoaderTest.class.getResourceAsStream("/assets/dinoworld/geo/triceratops.geo.json")),
                StandardCharsets.UTF_8)) {
            ModelPart root = GeoModelLoader.load(reader).bakeRoot();
            assertDoesNotThrow(() -> new TriceratopsModel(root));
        }
    }

    @Test
    void rotatedCubeBecomesChildPart() {
        ModelPart root = load("""
                {"name": "body", "pivot": [0, 0, 0], "cubes": [
                    {"origin": [0, 0, 0], "size": [1, 1, 1], "uv": [0, 0], "pivot": [0, 4, 0], "rotation": [90, 0, 0]}]}
                """).bakeRoot();
        ModelPart rotated = root.getChild("body").getChild("body_r1");
        assertEquals(-4.0f, rotated.y);
        assertEquals((float) Math.toRadians(90), rotated.xRot, 1e-6f);
    }

    @Test
    void rejectsGeneratedNameClash() {
        assertThrows(JsonParseException.class, () -> load("""
                {"name": "body", "cubes": [{"origin": [0, 0, 0], "size": [1, 1, 1], "uv": [0, 0], "rotation": [0, 0, 45]}]},
                {"name": "body_r1", "parent": "body"}
                """));
    }

    @Test
    void rejectsCubeWithoutSize() {
        assertThrows(JsonParseException.class, () -> load("""
                {"name": "body", "cubes": [{"origin": [0, 0, 0], "uv": [0, 0]}]}
                """));
    }

    private static net.minecraft.client.model.geom.builders.LayerDefinition load(String bones) {
        return GeoModelLoader.load(new StringReader("""
                {"minecraft:geometry": [{"description": {"texture_width": 16, "texture_height": 16}, "bones": [%s]}]}
                """.formatted(bones)));
    }
}
