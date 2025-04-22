package models.Animal;

import models.enums.Animal;

public class CageAnimals implements Animals {
    private Animal animal;

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
