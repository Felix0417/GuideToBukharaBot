package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface ButtonCommandStrategy {

    MenuButtonTags getKey();

    List<BotApiMethod> handle(Update update);
}
