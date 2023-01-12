package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CallbackStrategy {
    String getKey();

    List<BotApiMethod> handler(Update update);
}
