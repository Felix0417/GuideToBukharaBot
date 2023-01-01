package com.telegramBots.GuideToBukharaBot.service.strategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.HELP;
import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.SETTINGS;

@Component
public class HelpButtonStrategy extends AbstractButtonStrategy {

    private final ButtonsOfMenu buttons;

    public HelpButtonStrategy(ButtonsOfMenu buttons) {
        super(HELP);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {
        var message = new SendMessage();
        message.setText(buttons.helpCommand());
        message.setChatId(chatId);
        return message;
    }
}
