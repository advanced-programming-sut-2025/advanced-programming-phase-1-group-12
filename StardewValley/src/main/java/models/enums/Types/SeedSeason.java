package models.enums.Types;

import models.enums.Season;

import java.util.Arrays;
import java.util.List;

public enum SeedSeason {
    JAZZ_SEEDS(Arrays.asList(Season.SPRING), "Jazz Seeds"),
    CARROT_SEEDS(Arrays.asList(Season.SPRING), "Carrot Seeds"),
    CAULIFLOWER_SEEDS(Arrays.asList(Season.SPRING), "Cauliflower Seeds"),
    COFFEE_BEAN(Arrays.asList(Season.SPRING), "Coffee Bean"),
    GARLIC_SEEDS(Arrays.asList(Season.SPRING), "Garlic Seeds"),
    BEAN_STARTER(Arrays.asList(Season.SPRING), "Bean Starter"),
    KALE_SEEDS(Arrays.asList(Season.SPRING), "Kale Seeds"),
    PARSNIP_SEEDS(Arrays.asList(Season.SPRING), "Parsnip Seeds"),
    POTATO_SEEDS(Arrays.asList(Season.SPRING), "Potato Seeds"),
    RHUBARB_SEEDS(Arrays.asList(Season.SPRING), "Rhubarb Seeds"),
    STRAWBERRY_SEEDS(Arrays.asList(Season.SPRING), "Strawberry Seeds"),
    TULIP_BULB(Arrays.asList(Season.SPRING), "Tulip Bulb"),
    RICE_SHOOT(Arrays.asList(Season.SPRING), "Rice Shoot"),

    BLUEBERRY_SEEDS(Arrays.asList(Season.SUMMER), "Blueberry Seeds"),
    CORN_SEEDS(Arrays.asList(Season.SUMMER), "Corn Seeds"),
    HOPS_STARTER(Arrays.asList(Season.SUMMER), "Hops Starter"),
    PEPPER_SEEDS(Arrays.asList(Season.SUMMER), "Pepper Seeds"),
    MELON_SEEDS(Arrays.asList(Season.SUMMER), "Melon Seeds"),
    POPPY_SEEDS(Arrays.asList(Season.SUMMER), "Poppy Seeds"),
    RADISH_SEEDS(Arrays.asList(Season.SUMMER), "Radis Seeds"),
    RED_CABBAGE_SEEDS(Arrays.asList(Season.SUMMER), "Red Cabbage Seeds"),
    STARFRUIT_SEEDS(Arrays.asList(Season.SUMMER), "Starfruit Seeds"),
    SPANGLE_SEEDS(Arrays.asList(Season.SUMMER), "Spangle Seeds"),
    SUMMER_SQUASH_SEEDS(Arrays.asList(Season.SUMMER), "Summer Squash Seeds"),
    SUNFLOWER_SEEDS(Arrays.asList(Season.SUMMER), "Sunflower Seeds"),
    TOMATO_SEEDS(Arrays.asList(Season.SUMMER), "Tomato Seeds"),
    WHEAT_SEEDS(Arrays.asList(Season.SUMMER), "Wheat Seeds"),

    AMARANTH_SEEDS(Arrays.asList(Season.AUTUMN), "Amaranth Seeds"),
    ARTICHOKE_SEEDS(Arrays.asList(Season.AUTUMN), "Archooke Seeds"),
    BEET_SEEDS(Arrays.asList(Season.AUTUMN), "Beet Seeds"),
    BOK_CHOY_SEEDS(Arrays.asList(Season.AUTUMN), "Bok Choy Seeds"),
    BROCCOLI_SEEDS(Arrays.asList(Season.AUTUMN), "Broccoli Seeds"),
    CRANBERRY_SEEDS(Arrays.asList(Season.AUTUMN), "Cranberry Seeds"),
    EGGPLANT_SEEDS(Arrays.asList(Season.AUTUMN), "Eggplant Seeds"),
    FAIRY_SEEDS(Arrays.asList(Season.AUTUMN), "Fairy Seeds"),
    GRAPE_STARTER(Arrays.asList(Season.AUTUMN), "Grape Starter"),
    PUMPKIN_SEEDS(Arrays.asList(Season.AUTUMN), "Pumpkin Seeds"),
    YAM_SEEDS(Arrays.asList(Season.AUTUMN), "Yam Seeds"),
    RARE_SEED(Arrays.asList(Season.AUTUMN), "Rare Seeds"),

    POWDERMELON_SEEDS(Arrays.asList(Season.WINTER), "Powdermelon Seeds"),

    ANCIENT_SEEDS(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), "Ancient Seeds"),
    MIXED_SEEDS(Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), "Mixed Seeds"),;

    public final List<Season> seasons;
    public final String name;

    SeedSeason(List<Season> seasons, String name) {
        this.seasons = seasons;
        this.name = name;
    }

    public static SeedSeason stringSeed(String name){
        for(SeedSeason seed : SeedSeason.values()){
            if(seed.name.equals(name)){
                return seed;
            }
        }
        return null;
    }
}
