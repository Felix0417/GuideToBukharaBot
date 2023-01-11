package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy.newhandlerstrategy;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface CommandStrategy {
    String getKey();

    List<BotApiMethod> handler(Update update);
}
