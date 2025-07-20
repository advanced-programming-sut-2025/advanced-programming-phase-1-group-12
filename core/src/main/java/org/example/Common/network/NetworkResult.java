package org.example.Common.network;

import org.example.Common.models.Fundementals.Result;

public class NetworkResult<T> {
    private boolean success;
    private String message;
    private T data;
    private int statusCode;
    private String errorCode;
    private long timestamp;
    
    public NetworkResult() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public NetworkResult(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
        this.statusCode = success ? 200 : 400;
    }
    
    public NetworkResult(boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }
    
    public NetworkResult(boolean success, String message, T data, int statusCode) {
        this(success, message, data);
        this.statusCode = statusCode;
    }
    
    // Convert from existing Result class
    public static <T> NetworkResult<T> fromResult(org.example.Common.models.Fundementals.Result result) {
        return new NetworkResult<>(result.isSuccessful(), result.getMessage());
    }
    
    public static <T> NetworkResult<T> fromResult(org.example.Common.models.Fundementals.Result result, T data) {
        return new NetworkResult<>(result.isSuccessful(), result.getMessage(), data);
    }
    
    // Success factory methods
    public static <T> NetworkResult<T> success(String message) {
        return new NetworkResult<>(true, message, null, 200);
    }
    
    public static <T> NetworkResult<T> success(String message, T data) {
        return new NetworkResult<>(true, message, data, 200);
    }
    
    // Error factory methods
    public static <T> NetworkResult<T> error(String message) {
        return new NetworkResult<>(false, message, null, 400);
    }
    
    public static <T> NetworkResult<T> error(String message, int statusCode) {
        return new NetworkResult<>(false, message, null, statusCode);
    }
    
    public static <T> NetworkResult<T> error(String message, String errorCode) {
        NetworkResult<T> result = new NetworkResult<>(false, message, null, 400);
        result.setErrorCode(errorCode);
        return result;
    }
    
    // Authentication specific errors
    public static <T> NetworkResult<T> unauthorized(String message) {
        return new NetworkResult<>(false, message, null, 401);
    }
    
    public static <T> NetworkResult<T> forbidden(String message) {
        return new NetworkResult<>(false, message, null, 403);
    }
    
    public static <T> NetworkResult<T> notFound(String message) {
        return new NetworkResult<>(false, message, null, 404);
    }
    
    // Convert to existing Result class for compatibility
    public org.example.Common.models.Fundementals.Result toResult() {
        return new org.example.Common.models.Fundementals.Result(success, message);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
} 