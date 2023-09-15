package com.flutter.cbp.domain.liability;


public final class AccountLiabilityThreshold implements LiabilityThreshold{

    private final int accountId;

    private final int stakeFactor;

    private final double multiplier;

    public AccountLiabilityThreshold(int identifier,
                                     int stakeFactor,
                                     double multiplier){
        this.accountId = identifier;
        this.stakeFactor = stakeFactor;
        this.multiplier = multiplier;
    }

    public int getStakeFactor() {
        return this.stakeFactor;
    }

    @Override
    public LiabilityType getLiabilityType() {
        return LiabilityType.CUSTOMER;
    }

    @Override
    public int getIdentifier() {
        return accountId;
    }

    @Override
    public double calculateLiabilityThreshold() {
        return stakeFactor * multiplier;
    }

}
