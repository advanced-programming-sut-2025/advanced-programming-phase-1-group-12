package org.example.Common.models;

import java.util.*;

public class ReactionManager {
    private static ReactionManager instance;
    private List<Reaction> predefinedReactions;
    private Map<String, List<Reaction>> customReactions; // playerId -> custom reactions
    private List<Reaction> activeReactions;
    
    private ReactionManager() {
        predefinedReactions = new ArrayList<>();
        customReactions = new HashMap<>();
        activeReactions = new ArrayList<>();
        initializePredefinedReactions();
    }
    
    public static ReactionManager getInstance() {
        if (instance == null) {
            instance = new ReactionManager();
        }
        return instance;
    }
    
    private void initializePredefinedReactions() {
        // Add predefined reactions with emoji and text
        predefinedReactions.add(new Reaction("happy", "Happy!", "Emoji/Emojis000.png", false, null));
        predefinedReactions.add(new Reaction("sad", "Sad", "Emoji/Emojis001.png", false, null));
        predefinedReactions.add(new Reaction("angry", "Angry!", "Emoji/Emojis002.png", false, null));
        predefinedReactions.add(new Reaction("love", "Love!", "Emoji/Emojis003.png", false, null));
        predefinedReactions.add(new Reaction("laugh", "Haha!", "Emoji/Emojis004.png", false, null));
        predefinedReactions.add(new Reaction("wow", "Wow!", "Emoji/Emojis005.png", false, null));
        predefinedReactions.add(new Reaction("cool", "Cool!", "Emoji/Emojis006.png", false, null));
        predefinedReactions.add(new Reaction("thumbs_up", "👍", "Emoji/Emojis007.png", false, null));
        predefinedReactions.add(new Reaction("thumbs_down", "👎", "Emoji/Emojis008.png", false, null));
        predefinedReactions.add(new Reaction("heart", "❤️", "Emoji/Emojis009.png", false, null));
        predefinedReactions.add(new Reaction("star", "⭐", "Emoji/Emojis010.png", false, null));
        predefinedReactions.add(new Reaction("fire", "🔥", "Emoji/Emojis011.png", false, null));
        predefinedReactions.add(new Reaction("clap", "👏", "Emoji/Emojis012.png", false, null));
        predefinedReactions.add(new Reaction("rocket", "🚀", "Emoji/Emojis013.png", false, null));
        predefinedReactions.add(new Reaction("rainbow", "🌈", "Emoji/Emojis014.png", false, null));
        predefinedReactions.add(new Reaction("sun", "☀️", "Emoji/Emojis015.png", false, null));
        predefinedReactions.add(new Reaction("moon", "🌙", "Emoji/Emojis016.png", false, null));
        predefinedReactions.add(new Reaction("flower", "🌸", "Emoji/Emojis017.png", false, null));
        predefinedReactions.add(new Reaction("tree", "🌳", "Emoji/Emojis018.png", false, null));
        predefinedReactions.add(new Reaction("fish", "🐟", "Emoji/Emojis019.png", false, null));
        predefinedReactions.add(new Reaction("music", "🎵", "Emoji/Emojis020.png", false, null));
        predefinedReactions.add(new Reaction("food", "🍕", "Emoji/Emojis021.png", false, null));
        predefinedReactions.add(new Reaction("drink", "🥤", "Emoji/Emojis022.png", false, null));
        predefinedReactions.add(new Reaction("gift", "🎁", "Emoji/Emojis023.png", false, null));
        predefinedReactions.add(new Reaction("money", "💰", "Emoji/Emojis024.png", false, null));
        predefinedReactions.add(new Reaction("gem", "💎", "Emoji/Emojis025.png", false, null));
        predefinedReactions.add(new Reaction("sword", "⚔️", "Emoji/Emojis026.png", false, null));
        predefinedReactions.add(new Reaction("shield", "🛡️", "Emoji/Emojis027.png", false, null));
        predefinedReactions.add(new Reaction("magic", "✨", "Emoji/Emojis028.png", false, null));
        predefinedReactions.add(new Reaction("ghost", "👻", "Emoji/Emojis029.png", false, null));
        predefinedReactions.add(new Reaction("alien", "👽", "Emoji/Emojis030.png", false, null));
        predefinedReactions.add(new Reaction("robot", "🤖", "Emoji/Emojis031.png", false, null));
        predefinedReactions.add(new Reaction("ninja", "🥷", "Emoji/Emojis032.png", false, null));
        predefinedReactions.add(new Reaction("wizard", "🧙", "Emoji/Emojis033.png", false, null));
    }
    
    public List<Reaction> getPredefinedReactions() {
        return new ArrayList<>(predefinedReactions);
    }
    
    public List<Reaction> getCustomReactions(String playerId) {
        return customReactions.getOrDefault(playerId, new ArrayList<>());
    }
    
    public void addCustomReaction(String playerId, Reaction reaction) {
        if (!customReactions.containsKey(playerId)) {
            customReactions.put(playerId, new ArrayList<>());
        }
        customReactions.get(playerId).add(reaction);
    }
    
    public void removeCustomReaction(String playerId, String reactionId) {
        if (customReactions.containsKey(playerId)) {
            customReactions.get(playerId).removeIf(r -> r.getId().equals(reactionId));
        }
    }
    
    public void addActiveReaction(Reaction reaction) {
        activeReactions.add(reaction);
    }
    
    public void removeActiveReaction(Reaction reaction) {
        activeReactions.remove(reaction);
    }
    
    public List<Reaction> getActiveReactions() {
        return new ArrayList<>(activeReactions);
    }
    
    public void updateActiveReactions(float deltaTime) {
        try {
            System.out.println("DEBUG: updateActiveReactions() called with deltaTime: " + deltaTime);
            System.out.println("DEBUG: Current active reactions count: " + activeReactions.size());
            
            Iterator<Reaction> iterator = activeReactions.iterator();
            while (iterator.hasNext()) {
                Reaction reaction = iterator.next();
                System.out.println("DEBUG: Updating reaction: " + reaction.getId() + ", displayTime: " + reaction.getDisplayTime());
                
                reaction.update(deltaTime);
                
                if (reaction.isExpired()) {
                    System.out.println("DEBUG: Reaction " + reaction.getId() + " has expired, disposing and removing");
                    reaction.dispose();
                    iterator.remove();
                } else {
                    System.out.println("DEBUG: Reaction " + reaction.getId() + " still active, displayTime: " + reaction.getDisplayTime());
                }
            }
            
            System.out.println("DEBUG: updateActiveReactions() completed, remaining reactions: " + activeReactions.size());
        } catch (Exception e) {
            System.err.println("ERROR: Exception in updateActiveReactions(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void clearAllReactions() {
        for (Reaction reaction : activeReactions) {
            reaction.dispose();
        }
        activeReactions.clear();
    }
    
    public Reaction createReaction(String playerId, String text, String emojiPath) {
        String id = UUID.randomUUID().toString();
        Reaction reaction = new Reaction(id, text, emojiPath, true, playerId);
        reaction.loadTexture();
        return reaction;
    }
    
    public Reaction createPredefinedReaction(String playerId, String reactionId) {
        Reaction predefined = predefinedReactions.stream()
            .filter(r -> r.getId().equals(reactionId))
            .findFirst()
            .orElse(null);
        
        if (predefined != null) {
            Reaction reaction = new Reaction(
                UUID.randomUUID().toString(),
                predefined.getText(),
                predefined.getEmojiPath(),
                false,
                playerId
            );
            reaction.loadTexture();
            return reaction;
        }
        return null;
    }
}
