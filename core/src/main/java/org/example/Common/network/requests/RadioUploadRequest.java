package org.example.Common.network.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RadioUploadRequest {
    @JsonProperty("trackName")
    private String trackName;
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("fileData")
    private String fileData; // Base64 encoded audio file
    
    @JsonProperty("duration")
    private long duration;
    
    @JsonProperty("uploaderId")
    private String uploaderId;
    
    @JsonProperty("uploaderName")
    private String uploaderName;

    public RadioUploadRequest() {}

    public RadioUploadRequest(String trackName, String fileName, String fileData, long duration, String uploaderId, String uploaderName) {
        this.trackName = trackName;
        this.fileName = fileName;
        this.fileData = fileData;
        this.duration = duration;
        this.uploaderId = uploaderId;
        this.uploaderName = uploaderName;
    }

    // Getters and setters
    public String getTrackName() { return trackName; }
    public void setTrackName(String trackName) { this.trackName = trackName; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFileData() { return fileData; }
    public void setFileData(String fileData) { this.fileData = fileData; }
    
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    
    public String getUploaderId() { return uploaderId; }
    public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }
    
    public String getUploaderName() { return uploaderName; }
    public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }
}
