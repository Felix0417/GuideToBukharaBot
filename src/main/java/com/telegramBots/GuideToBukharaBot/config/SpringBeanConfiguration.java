package com.telegramBots.GuideToBukharaBot.config;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.service.strategies.ButtonStrategy;
import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

@Configuration
public class SpringBeanConfiguration {

    @Bean
    public Map<MenuButtonTags, ButtonStrategy> getButtonStrategiesList(List<ButtonStrategy> beans) {
        return StreamEx.of(beans).toMap(ButtonStrategy::getKey, identity());
    }
}
