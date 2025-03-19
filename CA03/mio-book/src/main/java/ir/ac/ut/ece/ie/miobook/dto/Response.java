package ir.ac.ut.ece.ie.miobook.dto;

import ir.ac.ut.ece.ie.miobook.model.MyLocalDateTime;

public class Response<T> {
    private String status;
    private MyLocalDateTime timestamp;
    private String message;
    private T data;

    public Response(String status, String message, T data) {
        this.status = status;
        this.timestamp = MyLocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}