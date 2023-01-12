package com.felixthecat.guideToTukharaBot.handler.handlerstrategy;

import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

@Configuration
public class CommandStrategyConfiguration {

    @Bean
    public Map<String, CommandStrategy> commandStrategyMap(List<CommandStrategy> commandStrategyList) {
        return StreamEx.of(commandStrategyList).toMap(CommandStrategy::getKey, identity());
    }
}
