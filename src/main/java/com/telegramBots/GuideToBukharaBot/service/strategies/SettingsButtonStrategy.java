package com.telegramBots.GuideToBukharaBot.service.strategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.SETTINGS;

@Component
public class SettingsButtonStrategy extends AbstractButtonStrategy {

    private final ButtonsOfMenu buttons;

    public SettingsButtonStrategy(ButtonsOfMenu buttons) {
        super(SETTINGS);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {

        var tags = buttons.changeUserStatus();
        var buttons = StreamEx.of(this.buttons.changeUserStatus())
                .skip(1L)
                .map(this::getButtonList).toList();

        var message = new SendMessage();
        message.setText(tags.get(0).getDescription());
        message.setChatId(chatId);
        message.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));
        return message;
    }
}
