package com.telegramBots.GuideToBukharaBot.service.strategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import com.telegramBots.GuideToBukharaBot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Function;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.ABOUT_BOT;

@Component
public class AboutBotButtonStrategy extends AbstractButtonStrategy {
    private final ButtonsOfMenu buttons;

    public AboutBotButtonStrategy(ButtonsOfMenu buttons) {
        super(ABOUT_BOT);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {
        var message = new SendMessage();
        message.setText(buttons.aboutBotCommand());
        message.setChatId(chatId);
        return message;
    }
}
