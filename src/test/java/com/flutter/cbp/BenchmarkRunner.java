package com.flutter.cbp;

import com.flutter.cbp.minhash.domain.Bet;
import info.debatty.java.lsh.LSHMinHash;
import info.debatty.java.lsh.MinHash;
import org.graalvm.collections.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.flutter.cbp.minhash.domain.Bet.BetBuilder;

public class BenchmarkRunner {
    //Important to provide a seed so that the same object always results in the same signature
    static int seed = 1;

    /**
     * Example for size 3
     * - object A1 has signature [20423, 30222, 48000]
     * - object A2 has signature [20423, 27990, 48000]
     * - this would result in similarity of 0.66666 (2/3)
     * Example for size 6
     * - object A1 has signature [20423, 30222, 48000, 19229, 91923, 88123]
     * - object A2 has signature [20423, 27990, 48000, 18220, 93123, 82300]
     * - in this example similarity falls to 0.33333 (2/6) for the same objects
     * <p>
     * As the signature size decreases, the potential for values to match increases, consequently leading to
     * greater similarity.
     */
    static int minHashSize = 5;

    //Still investigating use for this
    static int dict_size = 5;

    /**
     * This will take the minHash signature generated before and produce an array with locations.
     * Example for size 3
     * - object A1 has min hash signature [20423, 30222, 48000, 19229, 91923, 88123] - lsh can generatee [-2, 3, 6]
     * - note - LSH location size can be != from minhash signature size. Once again, bigger min hash signature
     * will lead to different locations for similar objects
     */
    static int lshLocationsSize = 3;

    /**
     * number of buckets that will be used to generate locations
     * if buckets == 4, means that lsh will generate locations between -3 and 3
     * would generate [0,-2, 3] but not [-7,2,10] for example
     */
    static int buckets = 60;

    /**
     * Size of the vector that LSH will sign. The size should be defined by min hash signature, but still investigating´
     * this one.
     */
    static int n = minHashSize;
    static MinHash minHash = new MinHash(minHashSize, dict_size, seed);
    static LSHMinHash lsh = new LSHMinHash(lshLocationsSize, buckets, 5, seed);


    @State(Scope.Thread)
    public static class BetState {
        public List<Bet> randomBets;
        public HashMap<Integer, List<int[]>> binnedBets;
        public HashMap<Integer, List<Bet>> hashedBets;

        @Setup(Level.Iteration)
        public void doSetup() {
            randomBets = generateRandomBets();
            binnedBets = binBetInLocations(randomBets);
            hashedBets = new HashMap<>();
            randomBets.forEach(bet -> {
                hashedBets.computeIfAbsent(bet.hashCode(), k -> new ArrayList<>()).add(bet);
            });
        }
    }


    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }


    @Benchmark
    @Fork(value = 3, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    public void testFindingMatchMinHash(BetState s) {
        //get random bet we know exists in the map
        Bet randomBet = s.randomBets.get(0);
        //check if it finds bet
        boolean found = checkMatch(s.binnedBets, randomBet);
        if (!found) System.out.println("Not found");
    }

    @Benchmark
    @Fork(value = 3, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    public void testFindingMatchNormalHash(BetState s) {
        //grab random bet we know exist
        Bet randomBet = s.randomBets.get(0);
        //check if it finds bet
        if (s.hashedBets.containsKey(randomBet.hashCode())) {
            s.hashedBets.get(randomBet.hashCode());
        } else System.out.println("Not found");
    }

    @Benchmark
    @Fork(value = 3, warmups = 1)
    @BenchmarkMode(Mode.AverageTime)
    public void testFindingMatchBruteForce(BetState s) {
        //grab last bet to simulate worst case scenario
        Bet randomBet = s.randomBets.get(s.randomBets.size() - 1);
        //check if it finds bet
        Optional<Bet> foundBet = s.randomBets
                .stream()
                .filter(b -> b.equals(randomBet))
                .findFirst();
        if (foundBet.isEmpty()) System.out.println("Not Found");
    }


    public static HashMap<Integer, List<int[]>> binBetInLocations(List<Bet> randomBets) {
        HashMap<Integer, List<int[]>> betsByLocation = new HashMap();
        for (Bet b : randomBets) {
            int[] signature = minHash.signature(new HashSet<>(b.getLegsHashCodes()));
            int[] locations = lsh.hashSignature(signature);
            Arrays.stream(locations)
                    .forEach(location -> betsByLocation.computeIfAbsent(location, k -> new ArrayList<>()).add(signature));
        }
        return betsByLocation;
    }

    public static List<Bet> generateRandomBets() {
        List<Bet> betList = new ArrayList<>();
        Random random = new Random();

        String[] competitions = {
                "Premier League", "La Liga", "Serie A", "Bundesliga", "Ligue 1", "Eredivisie", "Primeira Liga", "Süper Lig"
        };

        String[] teams = {
                "Manchester United", "Real Madrid", "Juventus", "Bayern Munich", "Paris Saint-Germain",
                "Liverpool", "Barcelona", "Inter Milan", "Borussia Dortmund", "Olympique Marseille",
                "Chelsea", "Atletico Madrid", "AC Milan", "RB Leipzig", "AS Monaco",
                "Tottenham Hotspur", "Valencia", "Napoli", "Schalke 04", "Lyon",
                "Arsenal", "Sevilla", "AS Roma", "VfL Wolfsburg", "Nice",
                "Manchester City", "Villarreal", "Fiorentina", "Hoffenheim", "Rennes",
                "Leeds United", "Real Betis", "Lazio", "Eintracht Frankfurt", "Lens"
        };

        for (int i = 0; i < 1000000; i++) {
            int accountId = random.nextInt(1000000); // Random account ID

            List<Pair<String, String>> games = new ArrayList<>();

            // Generate a random number of games between 2 and 5
            int numGames = random.nextInt(4) + 2;

            for (int j = 0; j < numGames; j++) {
                String competition = competitions[random.nextInt(competitions.length)];
                String team1 = teams[random.nextInt(teams.length)];
                String team2 = teams[random.nextInt(teams.length)];
                while (team1.equals(team2)) {
                    team2 = teams[random.nextInt(teams.length)];
                }
                Pair<String, String> gamePair = Pair.create(competition, team1 + " vs " + team2);
                games.add(gamePair);
            }

            Bet bet = BetBuilder(accountId, games);
            betList.add(bet);
        }

        return betList;
    }

    public static Bet createSimilarBet1ExtraLeg(Bet b) {
        List<Pair<String, String>> games = b
                .getLegs()
                .stream()
                .flatMap(leg ->
                        leg.getPart().
                                stream().
                                map(part ->
                                        Pair.create(
                                                part.getCompetitionInfo().getEntity().getName(),
                                                part.getEventInfo().getEntity().getName()
                                        )
                                )
                )
                .collect(Collectors.toList());
        Pair<String, String> gamePair = Pair.create("TESTING LEAGUE", "best team" + " vs " + "worst team");
        games.add(gamePair);
        return BetBuilder(b.getAccountId(), games);
    }

    public static Bet createSimilarBet1LessLeg(Bet b) {
        List<Pair<String, String>> games = b
                .getLegs()
                .stream()
                .flatMap(leg ->
                        leg.getPart().
                                stream().
                                map(part ->
                                        Pair.create(
                                                part.getCompetitionInfo().getEntity().getName(),
                                                part.getEventInfo().getEntity().getName()
                                        )
                                )
                )
                .collect(Collectors.toList());
        games.remove(0);
        return BetBuilder(b.getAccountId(), games);
    }

    public static boolean checkMatch(HashMap<Integer, List<int[]>> betsByLocation, Bet b) {
        int[] signature = minHash.signature(new HashSet<>(b.getLegsHashCodes()));
        int[] locations = lsh.hashSignature(signature);
        for (int location : locations) {
            if (betsByLocation.containsKey(location)) {
                List<int[]> sigs = betsByLocation.get(location);
                Optional<int[]> foundMatch = sigs.stream()
                        .filter(sig -> {
                            double similarity = minHash.similarity(sig, signature);
                            return similarity > 0.9;
                        }).findFirst();

                return foundMatch.isPresent();
            }
        }
        return false;
    }
}
