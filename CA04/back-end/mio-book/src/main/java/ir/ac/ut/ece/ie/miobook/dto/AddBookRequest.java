package ir.ac.ut.ece.ie.miobook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.ac.ut.ece.ie.miobook.exceptions.MissingRequiredFieldsException;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddBookRequest {
    private String author;
    private String title;
    private String publisher;
    private Integer year;
    private String price;
    private String synopsis;
    private String content;
    private List<String> genres;

    public void validate() {
        if (author == null || author.isEmpty()) {
            throw new MissingRequiredFieldsException("author");
        }
        if (title == null || title.isEmpty()) {
            throw new MissingRequiredFieldsException("title");
        }
        if (publisher == null || publisher.isEmpty()) {
            throw new MissingRequiredFieldsException("publisher");
        }
        if (year == null) {
            throw new MissingRequiredFieldsException("year");
        }
        if (price == null || price.isEmpty()) {
            throw new MissingRequiredFieldsException("price");
        }
        if (synopsis == null || synopsis.isEmpty()) {
            throw new MissingRequiredFieldsException("synopsis");
        }
        if (content == null || content.isEmpty()) {
            throw new MissingRequiredFieldsException("content");
        }
        if (genres == null || genres.isEmpty()) {
            throw new MissingRequiredFieldsException("genres");
        }
    }
}
