package com.telegramBots.GuideToBukharaBot.service.strategies.buttonStrategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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
        message.setChatId(String.valueOf(chatId));
        return message;
    }
}
