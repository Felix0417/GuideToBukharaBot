package com.felixthecat.guideToTukharaBot.dispatcher;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@FunctionalInterface
public interface MessageDispatcher {
    List<SendMessage> dispatch(Update update);
}
