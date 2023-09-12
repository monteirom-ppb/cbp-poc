package com.flutter.cbp;

import com.flutter.cbp.minhash.domain.Bet;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.UniformStickyPartitioner;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.graalvm.collections.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.flutter.cbp.minhash.domain.Bet.BetBuilder;

public class BetProducer {
    private static final Logger log = LoggerFactory.getLogger(BetProducer.class.getName());

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC = "bets_test";

    public static void main(String[] args) {
        List<Bet> randomBets = generateRandomBets();
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);
        KafkaSender<Integer, String> sender = KafkaSender.create(senderOptions);
        List<SenderRecord<Integer, String, Integer>> list = new ArrayList<>();
        for (Bet bet : randomBets) {
            list.add(SenderRecord.create(new ProducerRecord<>(TOPIC, bet.getAccountId(), bet.toString()), bet.getAccountId()));
        }
        Flux<SenderRecord<Integer, String, Integer>> senderRecordFlux = Flux.fromIterable(list);
        sender.send(senderRecordFlux).subscribe();
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

        //int[] accountIds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};

        int[] accountIds = IntStream.range(1, 100)
                .toArray();

        for (int i = 0; i < 10000; i++) {
            int accountId = accountIds[random.nextInt(accountIds.length)];

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

}
