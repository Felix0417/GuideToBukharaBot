package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

@Configuration
public class CallbackStrategyConfiguration {

    @Bean
    public Map<String, CallbackStrategy> getCallbackStrategyMap(List<CallbackStrategy> strategies) {
        return StreamEx.of(strategies).toMap(CallbackStrategy::getKey, identity());
    }
}
