package org.example.Common.network;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.saveGame.GenericKeyDeserializer;
import org.example.Common.saveGame.GenericKeySerializer;

@JsonIgnoreProperties({"region", "texture"})
abstract class TextureRegionMixIn {}

public class NetworkObjectMapper {
    private static final ObjectMapper instance = createConfiguredMapper();

    private static ObjectMapper createConfiguredMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Configure mapper to handle TextureRegion
        mapper.addMixIn(TextureRegion.class, TextureRegionMixIn.class);

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

        return mapper;
    }

    public static ObjectMapper getInstance() {
        return instance;
    }
}

