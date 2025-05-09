package com.onerty.yeogi.customer.config;

import com.onerty.yeogi.customer.reservation.TempReservationExpirationHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisExpirationListenerConfig {

    private final RedisMessageListenerContainer listenerContainer;
    private final TempReservationExpirationHandler expirationHandler;

    @PostConstruct
    public void init() {
        listenerContainer.addMessageListener(
                expirationHandler,
                new PatternTopic("__keyevent@0__:expired")
        );
    }
}
