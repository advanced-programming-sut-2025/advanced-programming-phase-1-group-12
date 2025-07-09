package org.example.models.Assets;

import com.badlogic.gdx.graphics.Texture;

public class AnimalAssetsManager {
    private static AnimalAssetsManager animalAssetsManager;

    public static AnimalAssetsManager animalAssetsManager() {
        if (animalAssetsManager == null) {
            animalAssetsManager = new AnimalAssetsManager();
        }
        return animalAssetsManager;
    }

    //animals themselves
    private final Texture duck = new Texture("Animals/duck.png");
    private final Texture chicken = new Texture("Animals/Brown_Chicken.png");
    private final Texture Rabbit = new Texture("Animals/Rabbit.png");
    private final Texture Dinosaur = new Texture("Animals/Dinosaur.png");
    private final Texture Cow = new Texture("Animals/White_Cow.png");
    private final Texture Pig = new Texture("Animals/Pig.png");
    private final Texture Sheep = new Texture("Animals/Sheep.png");
    private final Texture Goat = new Texture("Animals/Snake.png");

    //animal products
    private final Texture egg = new Texture("Animal_product/Egg.png");
    private final Texture largeEgg = new Texture("Animal_product/Large_Egg.png");

    private final Texture duckEgg = new Texture("Animal_product/Duck_Egg.png");
    private final Texture duckFeather = new Texture("Animal_product/Duck_Feather.png");

//TODO: is there a rabbit wool?!
    private final Texture wool = new Texture("Animal_product/Wool.png");
    private final Texture rabbitPie = new Texture("Recipe/Rhubarb_Pie.png");

    private final Texture dinosaurEgg = new Texture("Animal_product/Dinosaur_Egg.png");

    private final Texture milk = new Texture("Animal_product/Milk.png");
    private final Texture milkLarge = new Texture("Animal_product/Large_Milk_RU.png");

    private final Texture goatMilk = new Texture("Animal_product/Goat_Milk.png");
    private final Texture goatLargeMilk = new Texture("Animal_product/Large_Goat_Milk.png");
    private final Texture truffle = new Texture("Animal_product/Truffle.png");

    public static AnimalAssetsManager getAnimalAssetsManager() {
        return animalAssetsManager;
    }

    public Texture getDuck() {
        return duck;
    }

    public Texture getChicken() {
        return chicken;
    }

    public Texture getRabbit() {
        return Rabbit;
    }

    public Texture getDinosaur() {
        return Dinosaur;
    }

    public Texture getCow() {
        return Cow;
    }

    public Texture getPig() {
        return Pig;
    }

    public Texture getSheep() {
        return Sheep;
    }

    public Texture getGoat() {
        return Goat;
    }

    public Texture getEgg() {
        return egg;
    }

    public Texture getLargeEgg() {
        return largeEgg;
    }

    public Texture getDuckEgg() {
        return duckEgg;
    }

    public Texture getDuckFeather() {
        return duckFeather;
    }

    public Texture getWool() {
        return wool;
    }

    public Texture getRabbitPie() {
        return rabbitPie;
    }

    public Texture getDinosaurEgg() {
        return dinosaurEgg;
    }

    public Texture getMilk() {
        return milk;
    }

    public Texture getMilkLarge() {
        return milkLarge;
    }

    public Texture getGoatMilk() {
        return goatMilk;
    }

    public Texture getGoatLargeMilk() {
        return goatLargeMilk;
    }

    public Texture getTruffle() {
        return truffle;
    }
}
