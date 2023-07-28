package com.flutter.cbp;

import com.ppb.platform.sb.fbp.proto.event.BetDomainEventOuterClass;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class BetDomainEventDeserializer implements Deserializer<BetDomainEventOuterClass.BetDomainEvent> {

    private final Logger logger = LoggerFactory.getLogger(BetDomainEventDeserializer.class);

    @Override
    public BetDomainEventOuterClass.BetDomainEvent deserialize(String s, byte[] bytes) {
        BetDomainEventOuterClass.BetDomainEvent betDomainEvent = null;

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            betDomainEvent = BetDomainEventOuterClass.BetDomainEvent.parseDelimitedFrom(inputStream);
        } catch (Exception e) {
            logger.error("operation=deserialize, msg='Failed to deserialize payload'", e);
        }

        return betDomainEvent;
    }
}
