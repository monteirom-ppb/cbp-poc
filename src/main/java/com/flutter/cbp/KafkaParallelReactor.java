package com.flutter.cbp;

import io.confluent.parallelconsumer.ParallelConsumerOptions;
import io.confluent.parallelconsumer.ParallelStreamProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class KafkaParallelReactor {

    static CountDownLatch cl = new CountDownLatch(10000);


    public static void main(String[] args) {
        kafkaReactor();
        kafkaReactor();
        kafkaReactor();
    }

    public static void parallelKafka() {
        final Logger logger = LoggerFactory.getLogger(KafkaParallelReactor.class);
        Map<String, Object> consumerProps = new HashMap<String, Object>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testing_liability_consumer111");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");


        Consumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        var options = ParallelConsumerOptions.<String, String>builder()
                .ordering(ParallelConsumerOptions.ProcessingOrder.KEY)
                .maxConcurrency(1000)
                .consumer(consumer)
                .build();


        ParallelStreamProcessor<String, String> eosStreamProcessor =
                ParallelStreamProcessor.createEosStreamProcessor(options);

        eosStreamProcessor.subscribe(List.of("bets_test"));

        long started = System.currentTimeMillis();

        eosStreamProcessor.poll(record -> {
            String bet = record.value();
            logger.info("Consumed bet with ID {} and bet {} latch is {}", record.key(), bet, cl.getCount());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cl.countDown();
            if (cl.getCount() <= 0) {
                System.out.println("TIME IT TOOK: " + (System.currentTimeMillis() - started));
            }
        });
    }

    public static void kafkaReactor() {
        final Logger logger = LoggerFactory.getLogger(KafkaParallelReactor.class);
        Map<String, Object> consumerProps = new HashMap<String, Object>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "testing_liability_consumer01");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        ReceiverOptions<Integer, String> receiverOption =
                ReceiverOptions.<Integer, String>create(consumerProps)
                        .subscription(Collections.singleton("bets_test"));

        Flux<ReceiverRecord<Integer, String>> inboundBets = KafkaReceiver.create(receiverOption).receive();

        long started = System.currentTimeMillis();

        inboundBets.subscribe(record -> {
            String bet = record.value();
            logger.info("Consumed bet with ID {} and bet {} latch is {}", record.key(), bet, cl.getCount());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cl.countDown();
            if (cl.getCount() <= 0) {
                System.out.println("TIME IT TOOK: " + (System.currentTimeMillis() - started));
            }
        });
    }

}
