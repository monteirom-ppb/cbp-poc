package com.flutter.cbp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CbpApplicationConfig {

    @Bean(initMethod = "init")
    public Consumer betConsumer() {
        return new Consumer();
    }
}
