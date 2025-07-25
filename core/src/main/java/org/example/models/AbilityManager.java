package org.example.models;

import org.example.models.Fundementals.App;
import org.example.models.RelatedToUser.Ability;

import java.util.HashMap;
import java.util.Map;

public class AbilityManager {
    private Map<String, Integer> abilityProgress = new HashMap<>();

    public void increaseProgress(String abilityName, int amount) {
        abilityProgress.put(abilityName, abilityProgress.getOrDefault(abilityName, 0) + amount);
        Ability ability = App.getCurrentPlayerLazy().getAbilityByName(abilityName);
        ability.increaseLevel();
    }

    public int getProgress(String abilityName) {
        return abilityProgress.getOrDefault(abilityName, 0);
    }

    public void setProgress(String abilityName, int progress) {
        abilityProgress.put(abilityName, progress);
    }
}
