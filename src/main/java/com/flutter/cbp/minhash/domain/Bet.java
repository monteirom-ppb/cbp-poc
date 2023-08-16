package com.flutter.cbp.minhash.domain;

import com.flutter.cbp.minhash.domain.part.*;
import org.graalvm.collections.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Bet {
    private int accountId;
    private List<Leg> legs;

    public Bet(int accountId, List<Leg> legs) {
        this.accountId = accountId;
        this.legs = legs;
    }

    public static Bet BetBuilder(int accountId, List<Pair<String, String>> games) {
        HashMap<String, String> ids = new HashMap<>();
        ids.put("sportex", "123");
        ids.put("ppb", "456");
        MarketInfo marketInfo = new MarketInfo(new Entity("money line", ids));
        MarketTypeInfo marketTypeInfo = new MarketTypeInfo(new Entity("goals", ids));
        SportInfo sportInfo = new SportInfo(new Entity("Soccer", ids));

        List<Leg> legs = games.stream().map(game ->
                new Leg(List.of(new Part(
                        new SelectionInfo(new Entity(game.getRight(), ids)),
                        marketInfo,
                        sportInfo,
                        new CompetitionInfo(new Entity(game.getLeft(), ids)),
                        new EventInfo(new Entity(game.getRight(), ids), "123"),
                        marketTypeInfo,
                        null,
                        1)),
                        1
                )
        ).toList();

        return new Bet(accountId, legs);
    }

    public List<Integer> getLegsHashCodes() {
        return legs.stream()
                .flatMap(leg -> leg.getPart().stream().map(Part::getMinHashSignature))
                .map(concat -> accountId + concat)
                .map(String::hashCode)
                .collect(Collectors.toList());
    }

    public int getAccountId() {
        return accountId;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "accountId=" + accountId +
                ", legs=" + legs +
                '}';
    }
}
