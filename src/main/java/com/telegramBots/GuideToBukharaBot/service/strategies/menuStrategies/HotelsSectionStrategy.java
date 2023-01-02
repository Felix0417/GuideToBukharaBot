package com.telegramBots.GuideToBukharaBot.service.strategies.menuStrategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import com.telegramBots.GuideToBukharaBot.service.strategies.menuStrategies.AbstractSectionMenuButtonStrategy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;


import static com.telegramBots.GuideToBukharaBot.model.Tags.HOTELS_ITEM;

@Component
public class HotelsSectionStrategy extends AbstractSectionMenuButtonStrategy {

    private final ButtonsOfMenu buttons;

    public HotelsSectionStrategy(ButtonsOfMenu buttons) {
        super(HOTELS_ITEM);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, buttons.hotelsSectionMenu());
    }
}