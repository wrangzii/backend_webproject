package com.project.web.payload.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseObject implements Serializable {
    private String status;
    private String message;
    private Object data;

    public ResponseObject(String message) {
        this.message = message;
    }

    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseObject(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
