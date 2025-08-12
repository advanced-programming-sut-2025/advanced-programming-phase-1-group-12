package org.example.Common.network.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Common.network.NetworkResult;
import java.util.List;

public class RadioStationListResponse extends NetworkResult {
    @JsonProperty("stations")
    private List<RadioStation> stations;

    public RadioStationListResponse() {
        super();
    }

    public RadioStationListResponse(boolean success, String message, List<RadioStation> stations) {
        super(success, message);
        this.stations = stations;
    }

    // Getters and setters
    public List<RadioStation> getStations() { return stations; }
    public void setStations(List<RadioStation> stations) { this.stations = stations; }

    // Radio station data class
    public static class RadioStation {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("ownerId")
        private String ownerId;
        
        @JsonProperty("ownerName")
        private String ownerName;
        
        @JsonProperty("isPrivate")
        private boolean isPrivate;
        
        @JsonProperty("currentListeners")
        private int currentListeners;
        
        @JsonProperty("maxListeners")
        private int maxListeners;
        
        @JsonProperty("isPlaying")
        private boolean isPlaying;

        public RadioStation() {}

        public RadioStation(String id, String name, String ownerId, String ownerName, boolean isPrivate, int currentListeners, int maxListeners, boolean isPlaying) {
            this.id = id;
            this.name = name;
            this.ownerId = ownerId;
            this.ownerName = ownerName;
            this.isPrivate = isPrivate;
            this.currentListeners = currentListeners;
            this.maxListeners = maxListeners;
            this.isPlaying = isPlaying;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getOwnerId() { return ownerId; }
        public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
        
        public String getOwnerName() { return ownerName; }
        public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
        
        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
        
        public int getCurrentListeners() { return currentListeners; }
        public void setCurrentListeners(int currentListeners) { this.currentListeners = currentListeners; }
        
        public int getMaxListeners() { return maxListeners; }
        public void setMaxListeners(int maxListeners) { this.maxListeners = maxListeners; }
        
        public boolean isPlaying() { return isPlaying; }
        public void setPlaying(boolean isPlaying) { this.isPlaying = isPlaying; }
    }
}


