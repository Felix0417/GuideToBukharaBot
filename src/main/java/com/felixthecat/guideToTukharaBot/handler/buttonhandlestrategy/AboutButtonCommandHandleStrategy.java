package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.ABOUT_BOT;

@Component
@RequiredArgsConstructor
public class AboutButtonCommandHandleStrategy implements ButtonCommandStrategy {
    private final ButtonsOfMenu buttons;

    @Override
    public MenuButtonTags getKey() {
        return ABOUT_BOT;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();
        var result = new SendMessage();
        result.setText(buttons.aboutBotCommand());
        result.setChatId(String.valueOf(chatId));
        return List.of(result);
    }
}