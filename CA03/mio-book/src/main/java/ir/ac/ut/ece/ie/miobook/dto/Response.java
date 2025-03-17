package ir.ac.ut.ece.ie.miobook.dto;

import java.time.LocalDateTime;

public class Response<T> {
    private String status;
    private LocalDateTime timestamp;
    private String message;
    private T data;

    public Response(String status, String message, T data) {
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }
}