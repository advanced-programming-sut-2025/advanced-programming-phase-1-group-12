package org.example.Common.saveGame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.Place.Place;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.CraftingRecipe;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@JsonIgnoreProperties({"region", "texture"})
abstract class TextureRegionMixIn {}

public class GameSaveManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Configure mapper to handle TextureRegion
        mapper.addMixIn(TextureRegion.class, TextureRegionMixIn.class);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(MapperFeature.USE_ANNOTATIONS, true);

        // Pretty print the output
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Register custom key serializer and deserializer
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

    public static void saveGameCompressed(Game game, String filePath) {
        try (GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(filePath))) {
            mapper.writeValue(gos, game);
            savePlayerUsernames(game, "savePlayers");
            System.out.println("✅ Game saved compressed to " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Failed to save game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Game loadGameCompressed(String filePath) {
        try (GZIPInputStream gis = new GZIPInputStream(new FileInputStream(filePath))) {
            return mapper.readValue(gis, Game.class);
        } catch (IOException e) {
            System.err.println("❌ Failed to load game: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static void savePlayerUsernames(Game game, String savingUsersBaseFolder) {
        try {
            Path savingUsersFolder = Paths.get(savingUsersBaseFolder);
            if (!Files.exists(savingUsersFolder)) {
                Files.createDirectories(savingUsersFolder);
            }

            List<String> usernames = game.getPlayers().stream()
                .map(player -> player.getUser().getUserName())
                .collect(Collectors.toList());

            String fileName = game.getGameId() + ".json";
            Path filePath = savingUsersFolder.resolve(fileName);

            mapper.writeValue(filePath.toFile(), usernames);

            System.out.println("✅ Player usernames saved to " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Failed to save player usernames: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> loadPlayerUsernames(String fullFilePath) {
        try {
            File file = new File(fullFilePath);
            return mapper.readValue(
                file,
                mapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


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
    public static ObjectMapper getMapper() { return mapper; }
}
