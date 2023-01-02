package com.telegramBots.GuideToBukharaBot.service.strategies.menuStrategies;

import com.telegramBots.GuideToBukharaBot.model.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractSectionMenuButtonStrategy implements SectionMenuButtonStrategy {
    @Getter
    private Tags key;

}
