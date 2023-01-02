package com.telegramBots.GuideToBukharaBot.service.strategies.buttonStrategies;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractButtonStrategy implements ButtonStrategy {
    @Getter
    private MenuButtonTags key;
}
