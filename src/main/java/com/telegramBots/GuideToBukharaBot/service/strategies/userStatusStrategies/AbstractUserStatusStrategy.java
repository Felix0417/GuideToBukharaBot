package com.telegramBots.GuideToBukharaBot.service.strategies.userStatusStrategies;

import com.telegramBots.GuideToBukharaBot.model.Tags;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class AbstractUserStatusStrategy implements UserStatusStrategy{

    @Getter
    private Tags key;
}
