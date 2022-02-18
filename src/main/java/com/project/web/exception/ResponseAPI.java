package com.project.web.exception;

import java.io.Serializable;

public class ResponseAPI implements Serializable {
    private String status;
    private String message;
    private Object data;

    public ResponseAPI(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseAPI(String status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
    public ResponseAPI(String message) {
        this.status = null;
        this.message = message;
        this.data = null;
    }

    public ResponseAPI(Object data) {
        this.status = null;
        this.message = null;
        this.data = data;
    }
    
    public static ResponseAPI internalServerError(String message) {
        return new ResponseAPI("Internal Server Error!" ,message);
    }

    public static ResponseAPI badRequest(String message) {
        return new ResponseAPI("Bad request!" ,message);
    }

    public static ResponseAPI notFound(String message) {
        return new ResponseAPI("Not found!" ,message);
    }

    public static ResponseAPI ok(Object data) {
        return new ResponseAPI(data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
