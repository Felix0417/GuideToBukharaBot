package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

@Configuration
public class ButtonCommandStrategyConfiguration {

    @Bean
    public Map<MenuButtonTags, ButtonCommandStrategy> getButtonCommandStrategiesMap(List<ButtonCommandStrategy> strategyList) {
        return StreamEx.of(strategyList).toMap(ButtonCommandStrategy::getKey, identity());
    }
}
