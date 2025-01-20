package com.microservices.shared_utils.statusResponces;

public class StringStatusResponse {
    private Boolean success;
    private String message;
    private String data;

    public StringStatusResponse() {
    }

    public StringStatusResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public StringStatusResponse(Boolean success, String message, String data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StringStatusResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
