package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.Tags;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface TagCommandStrategy {
    Tags getKey();

    List<BotApiMethod> handle(Update update);
}
