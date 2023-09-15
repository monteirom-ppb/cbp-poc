package com.flutter.cbp.domain;

import com.flutter.cbp.domain.liability.AccountLiabilityThreshold;
import com.flutter.cbp.domain.liability.HierarchyLiabilityThreshold;
import com.flutter.cbp.domain.liability.LiabilityThreshold;
import com.flutter.cbp.domain.liability.LiabilityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class User {

    private final int accountId;

    private final List<String> productIds;

    private final AccountLiabilityThreshold accountLiabilityThreshold;

    private final Map<Integer, HierarchyLiabilityThreshold> sportLiabilityThreshold;

    private final Map<Integer, HierarchyLiabilityThreshold> competitionLiabilityThreshold;


    public User(int accountId, AccountLiabilityThreshold accountLiabilityThreshold) {
        this.accountId = accountId;
        this.productIds = new ArrayList<>();
        this.accountLiabilityThreshold = accountLiabilityThreshold;
        this.sportLiabilityThreshold = new HashMap<>();
        this.competitionLiabilityThreshold = new HashMap<>();
    }

    public LiabilityThreshold getLowestLiabilityThreshold(Set<Integer> sportIds,
                                                          Set<Integer> competitionIds) {
        return null;
    }

    public void changeCustomerLiabilityThreshold(LiabilityThreshold liabilityThreshold) {
        //TODO:
    }

    public void addSportLiabilityThreshold(LiabilityThreshold sportLiabilityThreshold) {
        //TODO:
    }

    public void addCompetitionLiabilityThreshold(LiabilityThreshold competitionLiabilityThreshold) {
        //TODOøØ
    }

}
