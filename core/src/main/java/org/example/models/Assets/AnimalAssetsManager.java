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

    private final Texture duck = new Texture("Animals/duck.png");
}
