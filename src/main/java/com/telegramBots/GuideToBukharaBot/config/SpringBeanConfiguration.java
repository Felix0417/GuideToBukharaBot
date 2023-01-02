package com.telegramBots.GuideToBukharaBot.config;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import com.telegramBots.GuideToBukharaBot.service.strategies.buttonStrategies.ButtonStrategy;
import com.telegramBots.GuideToBukharaBot.service.strategies.menuStrategies.SectionMenuButtonStrategy;
import com.telegramBots.GuideToBukharaBot.service.strategies.userStatusStrategies.UserStatusStrategy;
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

    @Bean
    public Map<Tags, SectionMenuButtonStrategy> getSectionMenuButtonStrategyList(List<SectionMenuButtonStrategy> strategies){
        return StreamEx.of(strategies).toMap(SectionMenuButtonStrategy::getKey, identity());
    }

    @Bean
    public Map<Tags, UserStatusStrategy> getUserStatusStrategyList(List<UserStatusStrategy> strategies){
        return StreamEx.of(strategies).toMap(UserStatusStrategy::getKey, identity());
    }
}
