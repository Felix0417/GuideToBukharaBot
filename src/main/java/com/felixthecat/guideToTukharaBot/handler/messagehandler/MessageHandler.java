package com.felixthecat.guideToTukharaBot.handler.messagehandler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface MessageHandler {

    boolean canHandle(Update update);

    List<BotApiMethod> handle(Update update);
}
