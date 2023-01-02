package com.felixthecat.guideToTukharaBot.dispatcher;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@FunctionalInterface
public interface MessageDispatcher {
    List<BotApiMethod> dispatch(Update update);
}
