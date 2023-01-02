package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.Tags;
import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

@Configuration
public class TagCommandStrategyConfiguration {

    @Bean
    public Map<Tags, TagCommandStrategy> getTagCommandStrategiesMap(List<TagCommandStrategy> strategyList) {
        return StreamEx.of(strategyList).toMap(TagCommandStrategy::getKey, identity());
    }
}
