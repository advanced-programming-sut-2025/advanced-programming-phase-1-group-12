package org.example.Common.network.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Common.network.NetworkResult;
import java.util.List;

public class RadioTrackListResponse extends NetworkResult {
    @JsonProperty("tracks")
    private List<RadioTrack> tracks;

    public RadioTrackListResponse() {
        super();
    }

    public RadioTrackListResponse(boolean success, String message, List<RadioTrack> tracks) {
        super(success, message);
        this.tracks = tracks;
    }

    // Getters and setters
    public List<RadioTrack> getTracks() { return tracks; }
    public void setTracks(List<RadioTrack> tracks) { this.tracks = tracks; }

    // Radio track data class
    public static class RadioTrack {
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("fileName")
        private String fileName;
        
        @JsonProperty("uploaderId")
        private String uploaderId;
        
        @JsonProperty("uploaderName")
        private String uploaderName;
        
        @JsonProperty("duration")
        private long duration;
        
        @JsonProperty("filePath")
        private String filePath;

        public RadioTrack() {}

        public RadioTrack(String id, String name, String fileName, String uploaderId, String uploaderName, long duration, String filePath) {
            this.id = id;
            this.name = name;
            this.fileName = fileName;
            this.uploaderId = uploaderId;
            this.uploaderName = uploaderName;
            this.duration = duration;
            this.filePath = filePath;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        
        public String getUploaderId() { return uploaderId; }
        public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }
        
        public String getUploaderName() { return uploaderName; }
        public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }
        
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
    }
}


