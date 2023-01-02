package com.telegramBots.GuideToBukharaBot.service.strategies.buttonStrategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.USER_DATA;

@Component
public class UserDataButtonStrategy extends AbstractButtonStrategy {

    private final ButtonsOfMenu buttons;

    public UserDataButtonStrategy(ButtonsOfMenu buttons) {
        super(USER_DATA);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {
        var message = new SendMessage();
        message.setText(buttons.userDataCommand(chatId));
        message.setChatId(String.valueOf(chatId));
        return message;
    }
}
