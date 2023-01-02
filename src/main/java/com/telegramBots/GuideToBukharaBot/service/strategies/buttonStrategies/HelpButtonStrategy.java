package com.telegramBots.GuideToBukharaBot.service.strategies.buttonStrategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.HELP;

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
        message.setChatId(String.valueOf(chatId));
        return message;
    }
}
