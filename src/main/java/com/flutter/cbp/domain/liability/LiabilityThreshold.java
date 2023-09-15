package com.flutter.cbp.domain.liability;

public interface LiabilityThreshold {

    LiabilityType getLiabilityType();

    int getIdentifier();

    double calculateLiabilityThreshold();
}
