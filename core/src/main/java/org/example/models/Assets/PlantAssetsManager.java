package org.example.models.Assets;

import com.badlogic.gdx.graphics.Texture;
import org.example.models.Fundementals.Location;
import org.example.models.enums.foraging.FertilizeType;
import org.example.models.enums.foraging.GiantPlants;
import org.example.models.enums.foraging.Plant;
import org.example.models.enums.foraging.TypeOfPlant;

import java.util.EnumMap;
import java.util.Map;

public class PlantAssetsManager {

    //Blue jazz
    public Texture Blue_jazz_stage_0 = new Texture("Crops/Blue_Jazz_Stage_1.png");
    public Texture Blue_Jazz_Stage_1 = new Texture("Crops/Blue_Jazz_Stage_2.png");
    public Texture Blue_Jazz_Stage_2 = new Texture("Crops/Blue_Jazz_Stage_3.png");
    public Texture Blue_Jazz_Stage_3 = new Texture("Crops/Blue_Jazz_Stage_4.png");
    public Texture Blue_Jazz_Stage_4 = new Texture("Crops/Blue_Jazz_Stage_5.png");

    //Carrot
    public Texture Carrot_Stage_0 = new Texture("Crops/Carrot_Stage_1.png");
    public Texture Carrot_Stage_1 = new Texture("Crops/Carrot_Stage_2.png");
    public Texture Carrot_Stage_2 = new Texture("Crops/Carrot_Stage_3.png");
    public Texture Carrot_Stage_3 = new Texture("Crops/Carrot_Stage_4.png");

    //Cauliflower
    public Texture Cauliflower_Stage_0 = new Texture("Crops/Cauliflower_Stage_1.png");
    public Texture Cauliflower_Stage_1 = new Texture("Crops/Cauliflower_Stage_2.png");
    public Texture Cauliflower_Stage_2 = new Texture("Crops/Cauliflower_Stage_3.png");
    public Texture Cauliflower_Stage_3 = new Texture("Crops/Cauliflower_Stage_4.png");
    public Texture Cauliflower_Stage_4 = new Texture("Crops/Cauliflower_Stage_5.png");
    public Texture Cauliflower_Stage_5 = new Texture("Crops/Cauliflower_Stage_6.png");

    //Coffee Bean
    public Texture Coffee_Stage_0 = new Texture("Crops/Coffee_Stage_1.png");
    public Texture Coffee_Stage_1 = new Texture("Crops/Coffee_Stage_2.png");
    public Texture Coffee_Stage_2 = new Texture("Crops/Coffee_Stage_3.png");
    public Texture Coffee_Stage_3 = new Texture("Crops/Coffee_Stage_4.png");
    public Texture Coffee_Stage_4 = new Texture("Crops/Coffee_Stage_5.png");
    public Texture Coffee_Stage_5 = new Texture("Crops/Coffee_Stage_6.png");

    //Garlic
    public Texture Garlic_Stage_0 = new Texture("Crops/Garlic_Stage_1.png");
    public Texture Garlic_Stage_1 = new Texture("Crops/Garlic_Stage_2.png");
    public Texture Garlic_Stage_2 = new Texture("Crops/Garlic_Stage_3.png");
    public Texture Garlic_Stage_3 = new Texture("Crops/Garlic_Stage_4.png");
    public Texture Garlic_Stage_4 = new Texture("Crops/Garlic_Stage_5.png");

    //Green_bean
    public Texture Green_Bean_Stage_0 = new Texture("Crops/Green_Bean_Stage_2.png");
    public Texture Green_Bean_Stage_1 = new Texture("Crops/Green_Bean_Stage_3.png");
    public Texture Green_Bean_Stage_2 = new Texture("Crops/Green_Bean_Stage_4.png");
    public Texture Green_Bean_Stage_3 = new Texture("Crops/Green_Bean_Stage_5.png");
    public Texture Green_Bean_Stage_4 = new Texture("Crops/Green_Bean_Stage_6.png");
    public Texture Green_Bean_Stage_5 = new Texture("Crops/Green_Bean_Stage_7.png");

    //Kale
    public Texture Kale_Stage_0 = new Texture("Crops/Kale_Stage_1.png");
    public Texture Kale_Stage_1 = new Texture("Crops/Kale_Stage_2.png");
    public Texture Kale_Stage_2 = new Texture("Crops/Kale_Stage_3.png");
    public Texture Kale_Stage_3 = new Texture("Crops/Kale_Stage_4.png");
    public Texture Kale_Stage_4 = new Texture("Crops/Kale_Stage_5.png");

    //Parsnip
    public Texture Parsnip_Stage_0 = new Texture("Crops/Parsnip_Stage_1.png");
    public Texture Parsnip_Stage_1 = new Texture("Crops/Parsnip_Stage_2.png");
    public Texture Parsnip_Stage_2 = new Texture("Crops/Parsnip_Stage_3.png");
    public Texture Parsnip_Stage_3 = new Texture("Crops/Parsnip_Stage_4.png");
    public Texture Parsnip_Stage_4 = new Texture("Crops/Parsnip_Stage_5.png");

    //Potato
    public Texture Potato_Stage_0 = new Texture("Crops/Potato_Stage_1.png");
    public Texture Potato_Stage_1 = new Texture("Crops/Potato_Stage_2.png");
    public Texture Potato_Stage_2 = new Texture("Crops/Potato_Stage_3.png");
    public Texture Potato_Stage_3 = new Texture("Crops/Potato_Stage_4.png");
    public Texture Potato_Stage_4 = new Texture("Crops/Potato_Stage_5.png");
    public Texture Potato_Stage_5 = new Texture("Crops/Potato_Stage_6.png");

    //Rhubarb
    public Texture Rhubarb_Stage_0 = new Texture("Crops/Rhubarb_Stage_1.png");
    public Texture Rhubarb_Stage_1 = new Texture("Crops/Rhubarb_Stage_2.png");
    public Texture Rhubarb_Stage_2 = new Texture("Crops/Rhubarb_Stage_3.png");
    public Texture Rhubarb_Stage_3 = new Texture("Crops/Rhubarb_Stage_4.png");
    public Texture Rhubarb_Stage_4 = new Texture("Crops/Rhubarb_Stage_5.png");
    public Texture Rhubarb_Stage_5 = new Texture("Crops/Rhubarb_Stage_6.png");

    //strawberry
    public Texture Strawberry_Stage_0 = new Texture("Crops/Strawberry_Stage_1.png");
    public Texture Strawberry_Stage_1 = new Texture("Crops/Strawberry_Stage_2.png");
    public Texture Strawberry_Stage_2 = new Texture("Crops/Strawberry_Stage_3.png");
    public Texture Strawberry_Stage_3 = new Texture("Crops/Strawberry_Stage_4.png");
    public Texture Strawberry_Stage_4 = new Texture("Crops/Strawberry_Stage_5.png");
    public Texture Strawberry_Stage_5 = new Texture("Crops/Strawberry_Stage_5.png");

    //Tulip
    public Texture Tulip_Stage_0 = new Texture("Crops/Tulip_Stage_1.png");
    public Texture Tulip_Stage_1 = new Texture("Crops/Tulip_Stage_2.png");
    public Texture Tulip_Stage_2 = new Texture("Crops/Tulip_Stage_3.png");
    public Texture Tulip_Stage_3 = new Texture("Crops/Tulip_Stage_4.png");
    public Texture Tulip_Stage_4 = new Texture("Crops/Tulip_Stage_6.png");

    //Unmilled_Rice
    public Texture Unmilled_Rice_Stage_0 = new Texture("Crops/Unmilled_Rice_Stage_1.png");
    public Texture Unmilled_Rice_Stage_1 = new Texture("Crops/Unmilled_Rice_Stage_2.png");
    public Texture Unmilled_Rice_Stage_2 = new Texture("Crops/Unmilled_Rice_Stage_3.png");
    public Texture Unmilled_Rice_Stage_3 = new Texture("Crops/Unmilled_Rice_Stage_4.png");
    public Texture Unmilled_Rice_Stage_4 = new Texture("Crops/Unmilled_Rice_Stage_5.png");

    //Blueberry
    public Texture Blueberry_Stage_0 = new Texture("Crops/Blueberry_Stage_1.png");
    public Texture Blueberry_Stage_1 = new Texture("Crops/Blueberry_Stage_2.png");
    public Texture Blueberry_Stage_2 = new Texture("Crops/Blueberry_Stage_3.png");
    public Texture Blueberry_Stage_3 = new Texture("Crops/Blueberry_Stage_4.png");
    public Texture Blueberry_Stage_4 = new Texture("Crops/Blueberry_Stage_5.png");
    public Texture Blueberry_Stage_5 = new Texture("Crops/Blueberry_Stage_6.png");

    //Corn
    public Texture Corn_Stage_0 = new Texture("Crops/Corn_Stage_1.png");
    public Texture Corn_Stage_1 = new Texture("Crops/Corn_Stage_2.png");
    public Texture Corn_Stage_2 = new Texture("Crops/Corn_Stage_3.png");
    public Texture Corn_Stage_3 = new Texture("Crops/Corn_Stage_4.png");
    public Texture Corn_Stage_4 = new Texture("Crops/Corn_Stage_5.png");
    public Texture Corn_Stage_5 = new Texture("Crops/Corn_Stage_6.png");

    //hops
    public Texture Hops_Stage_0 = new Texture("Crops/Hops_Stage_1.png");
    public Texture Hops_Stage_1 = new Texture("Crops/Hops_Stage_3.png");
    public Texture Hops_Stage_2 = new Texture("Crops/Hops_Stage_4.png");
    public Texture Hops_Stage_3 = new Texture("Crops/Hops_Stage_5.png");
    public Texture Hops_Stage_4 = new Texture("Crops/Hops_Stage_6.png");
    public Texture Hops_Stage_5 = new Texture("Crops/Hops_Stage_7.png");

    //Hot pepper
    public Texture Hot_Pepper_Stage_0 = new Texture("Crops/Hot_Pepper_Stage_1.png");
    public Texture Hot_Pepper_Stage_1 = new Texture("Crops/Hot_Pepper_Stage_2.png");
    public Texture Hot_Pepper_Stage_2 = new Texture("Crops/Hot_Pepper_Stage_3.png");
    public Texture Hot_Pepper_Stage_3 = new Texture("Crops/Hot_Pepper_Stage_4.png");
    public Texture Hot_Pepper_Stage_4 = new Texture("Crops/Hot_Pepper_Stage_4b.png");
    public Texture Hot_Pepper_Stage_5 = new Texture("Crops/Hot_Pepper_Stage_5.png");

    //Melon
    public Texture Melon_Stage_0 = new Texture("Crops/Melon_Stage_1.png");
    public Texture Melon_Stage_1 = new Texture("Crops/Melon_Stage_2.png");
    public Texture Melon_Stage_2 = new Texture("Crops/Melon_Stage_3.png");
    public Texture Melon_Stage_3 = new Texture("Crops/Melon_Stage_4.png");
    public Texture Melon_Stage_4 = new Texture("Crops/Melon_Stage_5.png");
    public Texture Melon_Stage_5 = new Texture("Crops/Melon_Stage_6.png");

    //Poppy
    public Texture Poppy_Stage_0 = new Texture("Crops/Poppy_Stage_1.png");
    public Texture Poppy_Stage_1 = new Texture("Crops/Poppy_Stage_2.png");
    public Texture Poppy_Stage_2 = new Texture("Crops/Poppy_Stage_3.png");
    public Texture Poppy_Stage_3 = new Texture("Crops/Poppy_Stage_4.png");
    public Texture Poppy_Stage_4 = new Texture("Crops/Poppy_Stage_6.png");

    //Radish
    public Texture Radish_Stage_0 = new Texture("Crops/Radish_Stage_1.png");
    public Texture Radish_Stage_1 = new Texture("Crops/Radish_Stage_2.png");
    public Texture Radish_Stage_2 = new Texture("Crops/Radish_Stage_3.png");
    public Texture Radish_Stage_3 = new Texture("Crops/Radish_Stage_4.png");
    public Texture Radish_Stage_4 = new Texture("Crops/Radish_Stage_5.png");

    //Red cabbage
    public Texture Red_Cabbage_Stage_0 = new Texture("Crops/Red_Cabbage_Stage_1.png");
    public Texture Red_Cabbage_Stage_1 = new Texture("Crops/Red_Cabbage_Stage_2.png");
    public Texture Red_Cabbage_Stage_2 = new Texture("Crops/Red_Cabbage_Stage_3.png");
    public Texture Red_Cabbage_Stage_3 = new Texture("Crops/Red_Cabbage_Stage_4.png");
    public Texture Red_Cabbage_Stage_4 = new Texture("Crops/Red_Cabbage_Stage_5.png");
    public Texture Red_Cabbage_Stage_5 = new Texture("Crops/Red_Cabbage_Stage_6.png");

    //Starfruit
    public Texture Starfruit_Stage_0 = new Texture("Crops/Starfruit_Stage_1.png");
    public Texture Starfruit_Stage_1 = new Texture("Crops/Starfruit_Stage_2.png");
    public Texture Starfruit_Stage_2 = new Texture("Crops/Starfruit_Stage_3.png");
    public Texture Starfruit_Stage_3 = new Texture("Crops/Starfruit_Stage_4.png");
    public Texture Starfruit_Stage_4 = new Texture("Crops/Starfruit_Stage_5.png");
    public Texture Starfruit_Stage_5 = new Texture("Crops/Starfruit_Stage_6.png");

    //Summer_Spangle
    public Texture Summer_Spangle_Stage_0 = new Texture("Crops/Summer_Spangle_Stage_1.png");
    public Texture Summer_Spangle_Stage_1 = new Texture("Crops/Summer_Spangle_Stage_2.png");
    public Texture Summer_Spangle_Stage_2 = new Texture("Crops/Summer_Spangle_Stage_3.png");
    public Texture Summer_Spangle_Stage_3 = new Texture("Crops/Summer_Spangle_Stage_4.png");
    public Texture Summer_Spangle_Stage_4 = new Texture("Crops/Summer_Spangle_Stage_5.png");

    //Summer squash
    public Texture Summer_Squash_Stage_0 = new Texture("Crops/Summer_Squash_Stage_1.png");
    public Texture Summer_Squash_Stage_1 = new Texture("Crops/Summer_Squash_Stage_2.png");
    public Texture Summer_Squash_Stage_2 = new Texture("Crops/Summer_Squash_Stage_3.png");
    public Texture Summer_Squash_Stage_3 = new Texture("Crops/Summer_Squash_Stage_4.png");
    public Texture Summer_Squash_Stage_4 = new Texture("Crops/Summer_Squash_Stage_5.png");
    public Texture Summer_Squash_Stage_5 = new Texture("Crops/Summer_Squash_Stage_6.png");

    //Sunflowers
    public Texture Sunflower_Stage_0 = new Texture("Crops/Sunflower_Stage_1.png");
    public Texture Sunflower_Stage_1 = new Texture("Crops/Sunflower_Stage_2.png");
    public Texture Sunflower_Stage_2 = new Texture("Crops/Sunflower_Stage_3.png");
    public Texture Sunflower_Stage_3 = new Texture("Crops/Sunflower_Stage_4.png");
    public Texture Sunflower_Stage_4 = new Texture("Crops/Sunflower_Stage_5.png");

    //Tomato
    public Texture Tomato_Stage_0 = new Texture("Crops/Tomato_Stage_1.png");
    public Texture Tomato_Stage_1 = new Texture("Crops/Tomato_Stage_2.png");
    public Texture Tomato_Stage_2 = new Texture("Crops/Tomato_Stage_3.png");
    public Texture Tomato_Stage_3 = new Texture("Crops/Tomato_Stage_4.png");
    public Texture Tomato_Stage_4 = new Texture("Crops/Tomato_Stage_5.png");
    public Texture Tomato_Stage_5 = new Texture("Crops/Tomato_Stage_6.png");

    //Wheat
    public Texture Wheat_Stage_0 = new Texture("Crops/Wheat_Stage_1.png");
    public Texture Wheat_Stage_1 = new Texture("Crops/Wheat_Stage_2.png");
    public Texture Wheat_Stage_2 = new Texture("Crops/Wheat_Stage_3.png");
    public Texture Wheat_Stage_3 = new Texture("Crops/Wheat_Stage_4.png");
    public Texture Wheat_Stage_4 = new Texture("Crops/Wheat_Stage_5.png");

    //Amaranth
    public Texture Amaranth_Stage_0 = new Texture("Crops/Amaranth_Stage_1.png");
    public Texture Amaranth_Stage_1 = new Texture("Crops/Amaranth_Stage_2.png");
    public Texture Amaranth_Stage_2 = new Texture("Crops/Amaranth_Stage_3.png");
    public Texture Amaranth_Stage_3 = new Texture("Crops/Amaranth_Stage_4.png");
    public Texture Amaranth_Stage_4 = new Texture("Crops/Amaranth_Stage_5.png");

    //Artichoke
    public Texture Artichoke_Stage_0 = new Texture("Crops/Artichoke_Stage_1.png");
    public Texture Artichoke_Stage_1 = new Texture("Crops/Artichoke_Stage_2.png");
    public Texture Artichoke_Stage_2 = new Texture("Crops/Artichoke_Stage_3.png");
    public Texture Artichoke_Stage_3 = new Texture("Crops/Artichoke_Stage_4.png");
    public Texture Artichoke_Stage_4 = new Texture("Crops/Artichoke_Stage_5.png");
    public Texture Artichoke_Stage_5 = new Texture("Crops/Artichoke_Stage_6.png");

    //Beet
    public Texture Beet_Stage_0 = new Texture("Crops/Beet_Stage_1.png");
    public Texture Beet_Stage_1 = new Texture("Crops/Beet_Stage_2.png");
    public Texture Beet_Stage_2 = new Texture("Crops/Beet_Stage_3.png");
    public Texture Beet_Stage_3 = new Texture("Crops/Beet_Stage_4.png");
    public Texture Beet_Stage_4 = new Texture("Crops/Beet_Stage_5.png");

    //Bok_Choy
    public Texture Bok_Choy_Stage_0 = new Texture("Crops/Bok_Choy_Stage_1.png");
    public Texture Bok_Choy_Stage_1 = new Texture("Crops/Bok_Choy_Stage_2.png");
    public Texture Bok_Choy_Stage_2 = new Texture("Crops/Bok_Choy_Stage_3.png");
    public Texture Bok_Choy_Stage_3 = new Texture("Crops/Bok_Choy_Stage_4.png");
    public Texture Bok_Choy_Stage_4 = new Texture("Crops/Bok_Choy_Stage_5.png");

    //Broccoli
    public Texture Broccoli_Stage_0 = new Texture("Crops/Broccoli_Stage_1.png");
    public Texture Broccoli_Stage_1 = new Texture("Crops/Broccoli_Stage_2.png");
    public Texture Broccoli_Stage_2 = new Texture("Crops/Broccoli_Stage_3.png");
    public Texture Broccoli_Stage_3 = new Texture("Crops/Broccoli_Stage_4.png");
    public Texture Broccoli_Stage_4 = new Texture("Crops/Broccoli_Stage_5.png");

    //Cranberry
    public Texture Cranberry_Stage_0 = new Texture("Crops/Cranberry_Stage_1.png");
    public Texture Cranberry_Stage_1 = new Texture("Crops/Cranberry_Stage_2.png");
    public Texture Cranberry_Stage_2 = new Texture("Crops/Cranberry_Stage_3.png");
    public Texture Cranberry_Stage_3 = new Texture("Crops/Cranberry_Stage_4.png");
    public Texture Cranberry_Stage_4 = new Texture("Crops/Cranberry_Stage_5.png");
    public Texture Cranberry_Stage_5 = new Texture("Crops/Cranberry_Stage_6.png");

    //Eggplant
    public Texture Eggplant_Stage_0 = new Texture("Crops/Eggplant_Stage_1.png");
    public Texture Eggplant_Stage_1 = new Texture("Crops/Eggplant_Stage_2.png");
    public Texture Eggplant_Stage_2 = new Texture("Crops/Eggplant_Stage_3.png");
    public Texture Eggplant_Stage_3 = new Texture("Crops/Eggplant_Stage_4.png");
    public Texture Eggplant_Stage_4 = new Texture("Crops/Eggplant_Stage_5.png");
    public Texture Eggplant_Stage_5 = new Texture("Crops/Eggplant_Stage_6.png");

    //Fairy_Rose
    public Texture Fairy_Rose_Stage_0 = new Texture("Crops/Fairy_Rose_Stage_1.png");
    public Texture Fairy_Rose_Stage_1 = new Texture("Crops/Fairy_Rose_Stage_2.png");
    public Texture Fairy_Rose_Stage_2 = new Texture("Crops/Fairy_Rose_Stage_3.png");
    public Texture Fairy_Rose_Stage_3 = new Texture("Crops/Fairy_Rose_Stage_4.png");
    public Texture Fairy_Rose_Stage_4 = new Texture("Crops/Fairy_Rose_Stage_5.png");

    //Grape
    public Texture Grape_Stage_0 = new Texture("Crops/Grape_Stage_1.png");
    public Texture Grape_Stage_1 = new Texture("Crops/Grape_Stage_2.png");
    public Texture Grape_Stage_2 = new Texture("Crops/Grape_Stage_3.png");
    public Texture Grape_Stage_3 = new Texture("Crops/Grape_Stage_4.png");
    public Texture Grape_Stage_4 = new Texture("Crops/Grape_Stage_5.png");
    public Texture Grape_Stage_5 = new Texture("Crops/Grape_Stage_6.png");

    //Pumpkin
    public Texture Pumpkin_Stage_0 = new Texture("Crops/Pumpkin_Stage_1.png");
    public Texture Pumpkin_Stage_1 = new Texture("Crops/Pumpkin_Stage_2.png");
    public Texture Pumpkin_Stage_2 = new Texture("Crops/Pumpkin_Stage_3.png");
    public Texture Pumpkin_Stage_3 = new Texture("Crops/Pumpkin_Stage_4.png");
    public Texture Pumpkin_Stage_4 = new Texture("Crops/Pumpkin_Stage_5.png");
    public Texture Pumpkin_Stage_5 = new Texture("Crops/Pumpkin_Stage_6.png");

    //Yam
    public Texture Yam_Stage_0 = new Texture("Crops/Yam_Stage_1.png");
    public Texture Yam_Stage_1 = new Texture("Crops/Yam_Stage_2.png");
    public Texture Yam_Stage_2 = new Texture("Crops/Yam_Stage_3.png");
    public Texture Yam_Stage_3 = new Texture("Crops/Yam_Stage_4.png");
    public Texture Yam_Stage_4 = new Texture("Crops/Yam_Stage_5.png");

    //Sweet_Gem_Berry
    public Texture Sweet_Gem_Berry_Stage_0 = new Texture("Crops/Sweet_Gem_Berry_Stage_1.png");
    public Texture Sweet_Gem_Berry_Stage_1 = new Texture("Crops/Sweet_Gem_Berry_Stage_2.png");
    public Texture Sweet_Gem_Berry_Stage_2 = new Texture("Crops/Sweet_Gem_Berry_Stage_3.png");
    public Texture Sweet_Gem_Berry_Stage_3 = new Texture("Crops/Sweet_Gem_Berry_Stage_4.png");
    public Texture Sweet_Gem_Berry_Stage_4 = new Texture("Crops/Sweet_Gem_Berry_Stage_5.png");
    public Texture Sweet_Gem_Berry_Stage_5 = new Texture("Crops/Sweet_Gem_Berry_Stage_6.png");

    //Powdermelon
    public Texture Powdermelon_Stage_0 = new Texture("Crops/Powdermelon_Stage_1.png");
    public Texture Powdermelon_Stage_1 = new Texture("Crops/Powdermelon_Stage_2.png");
    public Texture Powdermelon_Stage_2 = new Texture("Crops/Powdermelon_Stage_3.png");
    public Texture Powdermelon_Stage_3 = new Texture("Crops/Powdermelon_Stage_4.png");
    public Texture Powdermelon_Stage_4 = new Texture("Crops/Powdermelon_Stage_5.png");
    public Texture Powdermelon_Stage_5 = new Texture("Crops/Powdermelon_Stage_6.png");

    //Ancient_Fruit
    public Texture Ancient_Fruit_Stage_0 = new Texture("Crops/Ancient_Fruit_Stage_1.png");
    public Texture Ancient_Fruit_Stage_1 = new Texture("Crops/Ancient_Fruit_Stage_2.png");
    public Texture Ancient_Fruit_Stage_2 = new Texture("Crops/Ancient_Fruit_Stage_3.png");
    public Texture Ancient_Fruit_Stage_3 = new Texture("Crops/Ancient_Fruit_Stage_4.png");
    public Texture Ancient_Fruit_Stage_4 = new Texture("Crops/Ancient_Fruit_Stage_5.png");
    public Texture Ancient_Fruit_Stage_5 = new Texture("Crops/Ancient_Fruit_Stage_6.png");

    //Grass_starter
    public Texture Grass_Starter_Stage_0 = new Texture("Crafting/Grass_Starter.png");
    public Texture Grass_Starter_Stage_1 = new Texture("Crafting/Grass_Starter.png");
    public Texture Grass_Starter_Stage_2 = new Texture("Crafting/Grass_Starter.png");
    public Texture Grass_Starter_Stage_3 = new Texture("Crafting/Grass_Starter.png");

    //Cherry_Tree
    public Texture Apricot_Stage_0 = new Texture("Trees/Apricot_Stage_1.png");
    public Texture Apricot_Stage_1 = new Texture("Trees/Apricot_Stage_2.png");
    public Texture Apricot_Stage_2 = new Texture("Trees/Apricot_Stage_3.png");
    public Texture Apricot_Stage_3 = new Texture("Trees/Apricot_Stage_4.png");
    public Texture Apricot_Stage_4 = new Texture("Trees/Apricot_Stage_5_Fruit.png");

    //Cherry_Tree
    public Texture Cherry_Stage_0 = new Texture("Trees/Cherry_Stage_1.png");
    public Texture Cherry_Stage_1 = new Texture("Trees/Cherry_Stage_2.png");
    public Texture Cherry_Stage_2 = new Texture("Trees/Cherry_Stage_3.png");
    public Texture Cherry_Stage_3 = new Texture("Trees/Cherry_Stage_4.png");
    public Texture Cherry_Stage_4 = new Texture("Trees/Cherry_Stage_5_Fruit.png");

    //Banana_Tree
    public Texture Banana_Stage_0 = new Texture("Trees/Banana_Stage_1.png");
    public Texture Banana_Stage_1 = new Texture("Trees/Banana_Stage_2.png");
    public Texture Banana_Stage_2 = new Texture("Trees/Banana_Stage_3.png");
    public Texture Banana_Stage_3 = new Texture("Trees/Banana_Stage_4.png");
    public Texture Banana_Stage_4 = new Texture("Trees/Banana_Stage_5_Fruit.png");

    //Mango_Tree
    public Texture Mango_Stage_0 = new Texture("Trees/Mango_Stage_1.png");
    public Texture Mango_Stage_1 = new Texture("Trees/Mango_Stage_2.png");
    public Texture Mango_Stage_2 = new Texture("Trees/Mango_Stage_3.png");
    public Texture Mango_Stage_3 = new Texture("Trees/Mango_Stage_4.png");
    public Texture Mango_Stage_4 = new Texture("Trees/Mango_Stage_5_Fruit.png");

    //Orange_Tree
    public Texture Orange_Stage_0 = new Texture("Trees/Orange_Stage_1.png");
    public Texture Orange_Stage_1 = new Texture("Trees/Orange_Stage_2.png");
    public Texture Orange_Stage_2 = new Texture("Trees/Orange_Stage_3.png");
    public Texture Orange_Stage_3 = new Texture("Trees/Orange_Stage_4.png");
    public Texture Orange_Stage_4 = new Texture("Trees/Orange_Stage_5_Fruit.png");

    //Peach_Tree
    public Texture Peach_Stage_0 = new Texture("Trees/Peach_Stage_1.png");
    public Texture Peach_Stage_1 = new Texture("Trees/Peach_Stage_2.png");
    public Texture Peach_Stage_2 = new Texture("Trees/Peach_Stage_3.png");
    public Texture Peach_Stage_3 = new Texture("Trees/Peach_Stage_4.png");
    public Texture Peach_Stage_4 = new Texture("Trees/Peach_Stage_5_Fruit.png");

    //Apple_Tree
    public Texture Apple_Stage_0 = new Texture("Trees/Apple_Stage_1.png");
    public Texture Apple_Stage_1 = new Texture("Trees/Apple_Stage_2.png");
    public Texture Apple_Stage_2 = new Texture("Trees/Apple_Stage_3.png");
    public Texture Apple_Stage_3 = new Texture("Trees/Apple_Stage_4.png");
    public Texture Apple_Stage_4 = new Texture("Trees/Apple_Stage_5_Fruit.png");

    //Mystic_Tree
    public Texture Mystic_Stage_0 = new Texture("Trees/Mystic_Tree_Stage_1.png");
    public Texture Mystic_Stage_1 = new Texture("Trees/Mystic_Tree_Stage_2.png");
    public Texture Mystic_Stage_2 = new Texture("Trees/Mystic_Tree_Stage_3.png");
    public Texture Mystic_Stage_3 = new Texture("Trees/Mystic_Tree_Stage_4.png");
    public Texture Mystic_Stage_4 = new Texture("Trees/Mystic_Tree_Stage_5.png");

    //Oak_Tree
    public Texture Oak_Stage_0 = new Texture("Trees/Oak_Stage_1.png");
    public Texture Oak_Stage_1 = new Texture("Trees/Oak_Stage_2.png");
    public Texture Oak_Stage_2 = new Texture("Trees/Oak_Stage_3.png");
    public Texture Oak_Stage_3 = new Texture("Trees/Oak_Stage_4.png");
    public Texture Oak_Stage_4 = new Texture("Trees/Oak_Stage_4.png");

    //Maple_Tree
    public Texture Maple_Stage_0 = new Texture("Trees/Maple_Stage_1.png");
    public Texture Maple_Stage_1 = new Texture("Trees/Maple_Stage_2.png");
    public Texture Maple_Stage_2 = new Texture("Trees/Maple_Stage_3.png");
    public Texture Maple_Stage_3 = new Texture("Trees/Maple_Stage_4.png");
    public Texture Maple_Stage_4 = new Texture("Trees/Maple_Stage_4.png");

    //Pine_Tree
    public Texture Pine_Stage_0 = new Texture("Trees/Pine_Stage_1.png");
    public Texture Pine_Stage_1 = new Texture("Trees/Pine_Stage_2.png");
    public Texture Pine_Stage_2 = new Texture("Trees/Pine_Stage_3.png");
    public Texture Pine_Stage_3 = new Texture("Trees/Pine_Stage_4.png");
    public Texture Pine_Stage_4 = new Texture("Trees/Pine_Stage_4.png");

    //Mushroom_Tree
    public Texture Mushroom_Stage_0 = new Texture("Trees/MushroomTree_Stage_1.png");
    public Texture Mushroom_Stage_1 = new Texture("Trees/MushroomTree_Stage_2.png");
    public Texture Mushroom_Stage_2 = new Texture("Trees/MushroomTree_Stage_3.png");
    public Texture Mushroom_Stage_3 = new Texture("Trees/MushroomTree_Stage_4.png");
    public Texture Mushroom_Stage_4 = new Texture("Trees/MushroomTree_Stage_5.png");

    //Mahogany_Tree
    public Texture Mahogany_Stage_0 = new Texture("Trees/Mahogany_Stage_1.png");
    public Texture Mahogany_Stage_1 = new Texture("Trees/Mahogany_Stage_2.png");
    public Texture Mahogany_Stage_2 = new Texture("Trees/Mahogany_Stage_3.png");
    public Texture Mahogany_Stage_3 = new Texture("Trees/Mahogany_Stage_4.png");
    public Texture Mahogany_Stage_4 = new Texture("Trees/Mahogany_Stage_4.png");

    public static final Map<TypeOfPlant, Texture[]> PLANT_TEXTURES = new EnumMap<>(TypeOfPlant.class);

    public static Texture treeType(Location location) {

        Plant plant = (Plant) location.getObjectInTile();
        int stage = plant.getCurrentStage();
        TypeOfPlant tp = plant.getTypeOfPlant();

        if (PLANT_TEXTURES.isEmpty()) buildTextureMap();

        Texture[] textures = PLANT_TEXTURES.getOrDefault(tp, PLANT_TEXTURES.get(TypeOfPlant.ORANGE_TREE));

        if (stage < 0) stage = 0;
        if (stage >= textures.length) stage = textures.length - 1;
        return textures[stage];
    }

    public static void buildTextureMap() {

        PLANT_TEXTURES.put(TypeOfPlant.BLUE_JAZZ,
            new Texture[]{INSTANCE.Blue_jazz_stage_0, INSTANCE.Blue_Jazz_Stage_1,
                INSTANCE.Blue_Jazz_Stage_2, INSTANCE.Blue_Jazz_Stage_3,
                INSTANCE.Blue_Jazz_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.CARROT,
            new Texture[]{INSTANCE.Carrot_Stage_0, INSTANCE.Carrot_Stage_1,
                INSTANCE.Carrot_Stage_2, INSTANCE.Carrot_Stage_3});

        PLANT_TEXTURES.put(TypeOfPlant.CAULIFLOWER,
            new Texture[]{INSTANCE.Cauliflower_Stage_0, INSTANCE.Cauliflower_Stage_1,
                INSTANCE.Cauliflower_Stage_2, INSTANCE.Cauliflower_Stage_3,
                INSTANCE.Cauliflower_Stage_4, INSTANCE.Cauliflower_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.COFFEE_BEAN,
            new Texture[]{INSTANCE.Coffee_Stage_0, INSTANCE.Coffee_Stage_1,
                INSTANCE.Coffee_Stage_2, INSTANCE.Coffee_Stage_3,
                INSTANCE.Coffee_Stage_4, INSTANCE.Coffee_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.GARLIC,
            new Texture[]{INSTANCE.Garlic_Stage_0, INSTANCE.Garlic_Stage_1,
                INSTANCE.Garlic_Stage_2, INSTANCE.Garlic_Stage_3,
                INSTANCE.Garlic_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.GREEN_BEAN,
            new Texture[]{INSTANCE.Green_Bean_Stage_0, INSTANCE.Green_Bean_Stage_1,
                INSTANCE.Green_Bean_Stage_2, INSTANCE.Green_Bean_Stage_3,
                INSTANCE.Green_Bean_Stage_4, INSTANCE.Green_Bean_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.KALE,
            new Texture[]{INSTANCE.Kale_Stage_0, INSTANCE.Kale_Stage_1,
                INSTANCE.Kale_Stage_2, INSTANCE.Kale_Stage_3,
                INSTANCE.Kale_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.PARSNIP,
            new Texture[]{INSTANCE.Parsnip_Stage_0, INSTANCE.Parsnip_Stage_1,
                INSTANCE.Parsnip_Stage_2, INSTANCE.Parsnip_Stage_3,
                INSTANCE.Parsnip_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.POTATO,
            new Texture[]{INSTANCE.Potato_Stage_0, INSTANCE.Potato_Stage_1,
                INSTANCE.Potato_Stage_2, INSTANCE.Potato_Stage_3,
                INSTANCE.Potato_Stage_4, INSTANCE.Potato_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.RHUBARB,
            new Texture[]{INSTANCE.Rhubarb_Stage_0, INSTANCE.Rhubarb_Stage_1,
                INSTANCE.Rhubarb_Stage_2, INSTANCE.Rhubarb_Stage_3,
                INSTANCE.Rhubarb_Stage_4, INSTANCE.Rhubarb_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.STRAWBERRY,
            new Texture[]{INSTANCE.Strawberry_Stage_0, INSTANCE.Strawberry_Stage_1,
                INSTANCE.Strawberry_Stage_2, INSTANCE.Strawberry_Stage_3,
                INSTANCE.Strawberry_Stage_4, INSTANCE.Strawberry_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.TULIP,
            new Texture[]{INSTANCE.Tulip_Stage_0, INSTANCE.Tulip_Stage_1,
                INSTANCE.Tulip_Stage_2, INSTANCE.Tulip_Stage_3,
                INSTANCE.Tulip_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.UNMILLED_RICE,
            new Texture[]{INSTANCE.Unmilled_Rice_Stage_0, INSTANCE.Unmilled_Rice_Stage_1,
                INSTANCE.Unmilled_Rice_Stage_2, INSTANCE.Unmilled_Rice_Stage_3,
                INSTANCE.Unmilled_Rice_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.BLUEBERRY,
            new Texture[]{INSTANCE.Blueberry_Stage_0, INSTANCE.Blueberry_Stage_1,
                INSTANCE.Blueberry_Stage_2, INSTANCE.Blueberry_Stage_3,
                INSTANCE.Blueberry_Stage_4, INSTANCE.Blueberry_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.CORN,
            new Texture[]{INSTANCE.Corn_Stage_0, INSTANCE.Corn_Stage_1,
                INSTANCE.Corn_Stage_2, INSTANCE.Corn_Stage_3,
                INSTANCE.Corn_Stage_4, INSTANCE.Corn_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.HOPS,
            new Texture[]{INSTANCE.Hops_Stage_0, INSTANCE.Hops_Stage_1,
                INSTANCE.Hops_Stage_2, INSTANCE.Hops_Stage_3,
                INSTANCE.Hops_Stage_4, INSTANCE.Hops_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.HOT_PEPPER,
            new Texture[]{INSTANCE.Hot_Pepper_Stage_0, INSTANCE.Hot_Pepper_Stage_1,
                INSTANCE.Hot_Pepper_Stage_2, INSTANCE.Hot_Pepper_Stage_3,
                INSTANCE.Hot_Pepper_Stage_4, INSTANCE.Hot_Pepper_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.MELON,
            new Texture[]{INSTANCE.Melon_Stage_0, INSTANCE.Melon_Stage_1,
                INSTANCE.Melon_Stage_2, INSTANCE.Melon_Stage_3,
                INSTANCE.Melon_Stage_4, INSTANCE.Melon_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.POPPY,
            new Texture[]{INSTANCE.Poppy_Stage_0, INSTANCE.Poppy_Stage_1,
                INSTANCE.Poppy_Stage_2, INSTANCE.Poppy_Stage_3,
                INSTANCE.Poppy_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.RADISH,
            new Texture[]{INSTANCE.Radish_Stage_0, INSTANCE.Radish_Stage_1,
                INSTANCE.Radish_Stage_2, INSTANCE.Radish_Stage_3,
                INSTANCE.Radish_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.RED_CABBAGE,
            new Texture[]{INSTANCE.Red_Cabbage_Stage_0, INSTANCE.Red_Cabbage_Stage_1,
                INSTANCE.Red_Cabbage_Stage_2, INSTANCE.Red_Cabbage_Stage_3,
                INSTANCE.Red_Cabbage_Stage_4, INSTANCE.Red_Cabbage_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.STARFRUIT,
            new Texture[]{INSTANCE.Starfruit_Stage_0, INSTANCE.Starfruit_Stage_1,
                INSTANCE.Starfruit_Stage_2, INSTANCE.Starfruit_Stage_3,
                INSTANCE.Starfruit_Stage_4, INSTANCE.Starfruit_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.SUMMER_SPANGLE,
            new Texture[]{INSTANCE.Summer_Spangle_Stage_0, INSTANCE.Summer_Spangle_Stage_1,
                INSTANCE.Summer_Spangle_Stage_2, INSTANCE.Summer_Spangle_Stage_3,
                INSTANCE.Summer_Spangle_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.SUMMER_SQUASH,
            new Texture[]{INSTANCE.Summer_Squash_Stage_0, INSTANCE.Summer_Squash_Stage_1,
                INSTANCE.Summer_Squash_Stage_2, INSTANCE.Summer_Squash_Stage_3,
                INSTANCE.Summer_Squash_Stage_4, INSTANCE.Summer_Squash_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.SUNFLOWER,
            new Texture[]{INSTANCE.Sunflower_Stage_0, INSTANCE.Sunflower_Stage_1,
                INSTANCE.Sunflower_Stage_2, INSTANCE.Sunflower_Stage_3,
                INSTANCE.Sunflower_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.TOMATO,
            new Texture[]{INSTANCE.Tomato_Stage_0, INSTANCE.Tomato_Stage_1,
                INSTANCE.Tomato_Stage_2, INSTANCE.Tomato_Stage_3,
                INSTANCE.Tomato_Stage_4, INSTANCE.Tomato_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.WHEAT,
            new Texture[]{INSTANCE.Wheat_Stage_0, INSTANCE.Wheat_Stage_1,
                INSTANCE.Wheat_Stage_2, INSTANCE.Wheat_Stage_3,
                INSTANCE.Wheat_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.AMARANTH,
            new Texture[]{INSTANCE.Amaranth_Stage_0, INSTANCE.Amaranth_Stage_1,
                INSTANCE.Amaranth_Stage_2, INSTANCE.Amaranth_Stage_3,
                INSTANCE.Amaranth_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.ARTICHOKE,
            new Texture[]{INSTANCE.Artichoke_Stage_0, INSTANCE.Artichoke_Stage_1,
                INSTANCE.Artichoke_Stage_2, INSTANCE.Artichoke_Stage_3,
                INSTANCE.Artichoke_Stage_4, INSTANCE.Artichoke_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.BEET,
            new Texture[]{INSTANCE.Beet_Stage_0, INSTANCE.Beet_Stage_1,
                INSTANCE.Beet_Stage_2, INSTANCE.Beet_Stage_3,
                INSTANCE.Beet_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.BOK_CHOY,
            new Texture[]{INSTANCE.Bok_Choy_Stage_0, INSTANCE.Bok_Choy_Stage_1,
                INSTANCE.Bok_Choy_Stage_2, INSTANCE.Bok_Choy_Stage_3,
                INSTANCE.Bok_Choy_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.BROCCOLI,
            new Texture[]{INSTANCE.Broccoli_Stage_0, INSTANCE.Broccoli_Stage_1,
                INSTANCE.Broccoli_Stage_2, INSTANCE.Broccoli_Stage_3,
                INSTANCE.Broccoli_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.CRANBERRIES,
            new Texture[]{INSTANCE.Cranberry_Stage_0, INSTANCE.Cranberry_Stage_1,
                INSTANCE.Cranberry_Stage_2, INSTANCE.Cranberry_Stage_3,
                INSTANCE.Cranberry_Stage_4, INSTANCE.Cranberry_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.EGGPLANT,
            new Texture[]{INSTANCE.Eggplant_Stage_0, INSTANCE.Eggplant_Stage_1,
                INSTANCE.Eggplant_Stage_2, INSTANCE.Eggplant_Stage_3,
                INSTANCE.Eggplant_Stage_4, INSTANCE.Eggplant_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.FAIRY_ROSE,
            new Texture[]{INSTANCE.Fairy_Rose_Stage_0, INSTANCE.Fairy_Rose_Stage_1,
                INSTANCE.Fairy_Rose_Stage_2, INSTANCE.Fairy_Rose_Stage_3,
                INSTANCE.Fairy_Rose_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.GRAPE,
            new Texture[]{INSTANCE.Grape_Stage_0, INSTANCE.Grape_Stage_1,
                INSTANCE.Grape_Stage_2, INSTANCE.Grape_Stage_3,
                INSTANCE.Grape_Stage_4, INSTANCE.Grape_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.PUMPKIN,
            new Texture[]{INSTANCE.Pumpkin_Stage_0, INSTANCE.Pumpkin_Stage_1,
                INSTANCE.Pumpkin_Stage_2, INSTANCE.Pumpkin_Stage_3,
                INSTANCE.Pumpkin_Stage_4, INSTANCE.Pumpkin_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.YAM,
            new Texture[]{INSTANCE.Yam_Stage_0, INSTANCE.Yam_Stage_1,
                INSTANCE.Yam_Stage_2, INSTANCE.Yam_Stage_3,
                INSTANCE.Yam_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.SWEET_GEM_BERRY,
            new Texture[]{INSTANCE.Sweet_Gem_Berry_Stage_0, INSTANCE.Sweet_Gem_Berry_Stage_1,
                INSTANCE.Sweet_Gem_Berry_Stage_2, INSTANCE.Sweet_Gem_Berry_Stage_3,
                INSTANCE.Sweet_Gem_Berry_Stage_4, INSTANCE.Sweet_Gem_Berry_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.POWDERMELON,
            new Texture[]{INSTANCE.Powdermelon_Stage_0, INSTANCE.Powdermelon_Stage_1,
                INSTANCE.Powdermelon_Stage_2, INSTANCE.Powdermelon_Stage_3,
                INSTANCE.Powdermelon_Stage_4, INSTANCE.Powdermelon_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.ANCIENT_FRUIT,
            new Texture[]{INSTANCE.Ancient_Fruit_Stage_0, INSTANCE.Ancient_Fruit_Stage_1,
                INSTANCE.Ancient_Fruit_Stage_2, INSTANCE.Ancient_Fruit_Stage_3,
                INSTANCE.Ancient_Fruit_Stage_4, INSTANCE.Ancient_Fruit_Stage_5});

        PLANT_TEXTURES.put(TypeOfPlant.GRASS_STARTER,
            new Texture[]{INSTANCE.Grass_Starter_Stage_0, INSTANCE.Grass_Starter_Stage_1,
                INSTANCE.Grass_Starter_Stage_2, INSTANCE.Grass_Starter_Stage_3});

        PLANT_TEXTURES.put(TypeOfPlant.APRICOT_TREE,
            new Texture[]{INSTANCE.Apricot_Stage_0, INSTANCE.Apricot_Stage_1,
                INSTANCE.Apricot_Stage_2, INSTANCE.Apricot_Stage_3,
                INSTANCE.Apricot_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.CHERRY_TREE,
            new Texture[]{INSTANCE.Cherry_Stage_0, INSTANCE.Cherry_Stage_1,
                INSTANCE.Cherry_Stage_2, INSTANCE.Cherry_Stage_3,
                INSTANCE.Cherry_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.PEACH_TREE,
            new Texture[]{INSTANCE.Peach_Stage_0, INSTANCE.Peach_Stage_1,
                INSTANCE.Peach_Stage_2, INSTANCE.Peach_Stage_3,
                INSTANCE.Peach_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.ORANGE_TREE,
            new Texture[]{INSTANCE.Orange_Stage_0, INSTANCE.Orange_Stage_1,
                INSTANCE.Orange_Stage_2, INSTANCE.Orange_Stage_3,
                INSTANCE.Orange_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.APPLE_TREE,
            new Texture[]{INSTANCE.Apple_Stage_0, INSTANCE.Apple_Stage_1,
                INSTANCE.Apple_Stage_2, INSTANCE.Apple_Stage_3,
                INSTANCE.Apple_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.BANANA_TREE,
            new Texture[]{INSTANCE.Banana_Stage_0, INSTANCE.Banana_Stage_1,
                INSTANCE.Banana_Stage_2, INSTANCE.Banana_Stage_3,
                INSTANCE.Banana_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.MANGO_TREE,
            new Texture[]{INSTANCE.Mango_Stage_0, INSTANCE.Mango_Stage_1,
                INSTANCE.Mango_Stage_2, INSTANCE.Mango_Stage_3,
                INSTANCE.Mango_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.MYSTIC_TREE,
            new Texture[]{INSTANCE.Mystic_Stage_0, INSTANCE.Mystic_Stage_1,
                INSTANCE.Mystic_Stage_2, INSTANCE.Mystic_Stage_3,
                INSTANCE.Mystic_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.OAK_TREE,
            new Texture[]{INSTANCE.Oak_Stage_0, INSTANCE.Oak_Stage_1,
                INSTANCE.Oak_Stage_2, INSTANCE.Oak_Stage_3,
                INSTANCE.Oak_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.MAPLE_TREE,
            new Texture[]{INSTANCE.Maple_Stage_0, INSTANCE.Maple_Stage_1,
                INSTANCE.Maple_Stage_2, INSTANCE.Maple_Stage_3,
                INSTANCE.Maple_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.PINE_TREE,
            new Texture[]{INSTANCE.Pine_Stage_0, INSTANCE.Pine_Stage_1,
                INSTANCE.Pine_Stage_2, INSTANCE.Pine_Stage_3,
                INSTANCE.Pine_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.MUSHROOM_TREE,
            new Texture[]{INSTANCE.Mushroom_Stage_0, INSTANCE.Mushroom_Stage_1,
                INSTANCE.Mushroom_Stage_2, INSTANCE.Mushroom_Stage_3,
                INSTANCE.Mushroom_Stage_4});

        PLANT_TEXTURES.put(TypeOfPlant.MAHOGANY_TREE,
            new Texture[]{INSTANCE.Mahogany_Stage_0, INSTANCE.Mahogany_Stage_1,
                INSTANCE.Mahogany_Stage_2, INSTANCE.Mahogany_Stage_3,
                INSTANCE.Mahogany_Stage_4});
    }

    private static final PlantAssetsManager INSTANCE = new PlantAssetsManager();

    public static Texture getGiantPlant(GiantPlants giantPlants) {

        switch (giantPlants){
            case CAULIFLOWER -> {
                return new Texture("Crops/Giant_Cauliflower.png");
            }
            case PUMPKIN -> {
                return new Texture("Crops/Giant_Pumpkin.png");
            }
            case POWDERMELON -> {
                return new Texture("Crops/Giant_Powdermelon.png");
            }
            default -> {
                return new Texture("Crops/Giant_Melon.png");
            }
        }
    }

    public static Texture getFertilizer(FertilizeType fertilizeType){
        switch (fertilizeType){
            case BasicRetainingSoil -> {
                return new Texture("Fertilizer/Basic_Retaining_Soil.png");
            }
            case QualityRetainingSoil -> {
                return new Texture("Fertilizer/Deluxe_Retaining_Soil.png");
            }
            default -> {
                return new Texture("Fertilizer/Quality_Retaining_Soil.png");
            }
        }
    }
}
