package com.flutter.cbp.minhash.domain;

import com.flutter.cbp.minhash.domain.part.Part;

import java.util.List;

public class Leg {

    private List<Part> part;
    private int legNumber;

    public Leg(List<Part> part, int legNumber) {
        this.part = part;
        this.legNumber = legNumber;
    }

    public List<Part> getPart() {
        return part;
    }

    public int getLegNumber() {
        return legNumber;
    }

    @Override
    public String toString() {
        return "Leg{" +
                "part=" + part +
                ", legNumber=" + legNumber +
                '}';
    }
}
