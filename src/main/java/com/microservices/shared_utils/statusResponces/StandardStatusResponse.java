package com.microservices.shared_utils.statusResponces;

public class StandardStatusResponse {
    public Boolean success;
    public String message;
    public Object data;

    public StandardStatusResponse() {
    }

    public StandardStatusResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public StandardStatusResponse(Boolean success, String message, Object data) {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StandardStatusResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
