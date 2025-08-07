package org.example.Common.saveGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.CraftingRecipe;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@JsonIgnoreProperties({ "region", "texture" })
abstract class TextureRegionMixIn {}

public final class GameSaveManager {
    private GameSaveManager() {}

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.addMixIn(TextureRegion.class, TextureRegionMixIn.class);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        SimpleModule module = new SimpleModule();
        module.addKeySerializer(Item.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Item.class, new GenericKeyDeserializer());
        module.addKeySerializer(CraftingRecipe.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(CraftingRecipe.class, new GenericKeyDeserializer());
        module.addKeySerializer(Cooking.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Cooking.class, new GenericKeyDeserializer());
        module.addKeySerializer(User.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(User.class, new GenericKeyDeserializer());
        module.addKeySerializer(Farm.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Farm.class, new GenericKeyDeserializer());
        module.addKeySerializer(Player.class, new GenericKeySerializer<>());
        module.addKeyDeserializer(Player.class, new GenericKeyDeserializer());
        mapper.registerModule(module);
    }

    public static ObjectMapper getMapper() { return mapper; }

    public static byte[] serializeAndCompress(Game game) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPOutputStream gos = new GZIPOutputStream(baos)) {
            mapper.writeValue(gos, game);
            gos.finish();
            return baos.toByteArray();
        }
    }

    public static Game decompressAndDeserialize(byte[] data) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(data))) {
            return mapper.readValue(gis, Game.class);
        }
    }

    public static void saveGameCompressed(Game game, String filePath) {
        try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(filePath))) {
            mapper.writeValue(gos, game);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Game loadGameCompressed(String filePath) {
        try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(filePath))) {
            return mapper.readValue(gis, Game.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
