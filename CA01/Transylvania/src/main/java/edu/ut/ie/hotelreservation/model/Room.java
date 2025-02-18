package edu.ut.ie.hotelreservation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Room {

    private int id;
    private int capacity;

    public Room(int id, int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.id = id;
        this.capacity = capacity;
    }
}
