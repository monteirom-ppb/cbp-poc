package com.flutter.cbp.minhash.domain.part;

public class FractionalPrice {
    private Fraction raw;
    private Fraction marginated;
    private Fraction current;

    @Override
    public String toString() {
        return "FractionalPrice{" +
                "raw=" + raw +
                ", marginated=" + marginated +
                ", current=" + current +
                '}';
    }
}
