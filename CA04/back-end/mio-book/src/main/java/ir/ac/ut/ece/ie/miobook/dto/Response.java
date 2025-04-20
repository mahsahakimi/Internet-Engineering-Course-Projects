package ir.ac.ut.ece.ie.miobook.dto;

import ir.ac.ut.ece.ie.miobook.model.MyLocalDateTime;

public class Response {
    public String status;
    public String timestamp;
    public String message;
    public Object data;

    public Response(String status, String message, Object data) {
        this.status = status;
        this.timestamp = MyLocalDateTime.now().toString();
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