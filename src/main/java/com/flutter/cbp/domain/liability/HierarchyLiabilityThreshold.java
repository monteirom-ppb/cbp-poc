package com.flutter.cbp.domain.liability;

public class HierarchyLiabilityThreshold implements LiabilityThreshold {

    private final int hierarchyId;

    private final int liabilityThreshold;

    private final LiabilityType liabilityType;

    public HierarchyLiabilityThreshold(int hierarchyId,
                                       int liabilityThreshold,
                                       LiabilityType liabilityType) {
        this.hierarchyId = hierarchyId;
        this.liabilityThreshold = liabilityThreshold;
        this.liabilityType = liabilityType;
    }

    @Override
    public LiabilityType getLiabilityType() {
        return liabilityType;
    }

    @Override
    public int getIdentifier() {
        return hierarchyId;
    }

    @Override
    public double calculateLiabilityThreshold() {
        return liabilityThreshold;
    }
}
