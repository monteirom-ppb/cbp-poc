package com.flutter.cbp;


import com.ppb.platform.sb.fbp.proto.event.BetDomainEventOuterClass;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(BetDomainEventDeserializer.class);


    public void init() {
        Map<String, Object> consumerProps = new HashMap<String, Object>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "use1-betting-backbone01.dev.fndlsb.net:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testing_liability_consumer");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BetDomainEventDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        ReceiverOptions<String, BetDomainEventOuterClass.BetDomainEvent> receiverOption =
                ReceiverOptions.<String, BetDomainEventOuterClass.BetDomainEvent>create(consumerProps)
                        .subscription(Collections.singleton("sb_aggregated_bet_processor_topic"));

        Flux<ReceiverRecord<String, BetDomainEventOuterClass.BetDomainEvent>> inboundBets = KafkaReceiver.create(receiverOption).receive();

        inboundBets.subscribe(bet -> {
            boolean betHasCompetitionId = bet.value().getBet().getLegsList()
                    .stream()
                    .anyMatch(leg -> leg.getPartList().stream().anyMatch(part -> !part.getCompetitionInfo().getEntity().getIdsMap().isEmpty()));

            logger.info("Consumed bet with ID {} and bet {} and has Ids on competition {}", bet.value().getBet().getBetId(), bet.value().getBet(), betHasCompetitionId);
            bet.receiverOffset().commit();
        });
    }

}
