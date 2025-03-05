package ir.ac.ut.ece.ie.miobook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Address {
    String country;
    String city;

    public Address(@JsonProperty("country") String country, @JsonProperty("city") String city) {
        this.country = country;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

