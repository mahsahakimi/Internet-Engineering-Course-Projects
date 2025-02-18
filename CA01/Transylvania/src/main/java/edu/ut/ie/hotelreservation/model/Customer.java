package edu.ut.ie.hotelreservation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Customer {

    private int ssn;
    private String name;
    private String phone;
    private int age;

    public Customer(int ssn, String name, String phoneNumber, int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
        this.ssn = ssn;
        this.name = name;
        this.phone = phoneNumber;
        this.age = age;
    }
}
