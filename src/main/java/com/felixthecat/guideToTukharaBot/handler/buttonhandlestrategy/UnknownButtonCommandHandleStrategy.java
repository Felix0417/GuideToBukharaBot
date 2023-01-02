package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class UnknownButtonCommandHandleStrategy implements ButtonCommandStrategy {
    @Override
    public MenuButtonTags getKey() {
        return null;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();
        var result = new SendMessage();
        result.setChatId(String.valueOf(chatId));
        result.setText(MenuButtonTags.WRONG_REQUEST_FROM_USER.getDescription());
        return List.of(result);
    }
}
