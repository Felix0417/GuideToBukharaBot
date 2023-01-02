package com.telegramBots.GuideToBukharaBot.service.strategies.menuStrategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.telegramBots.GuideToBukharaBot.model.Tags.ATTRACTIONS_ITEM;

@Component
public class AttractionSectionStrategy extends AbstractSectionMenuButtonStrategy{

    ButtonsOfMenu buttons;

    public AttractionSectionStrategy(ButtonsOfMenu buttons) {
        super(ATTRACTIONS_ITEM);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, buttons.attractionSectionMenu());
    }
}
