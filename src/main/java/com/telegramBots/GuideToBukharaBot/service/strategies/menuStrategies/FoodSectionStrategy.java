package com.telegramBots.GuideToBukharaBot.service.strategies.menuStrategies;

import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.telegramBots.GuideToBukharaBot.model.Tags.FOOD_ITEM;

@Component
public class FoodSectionStrategy extends AbstractSectionMenuButtonStrategy{

    ButtonsOfMenu buttons;

    public FoodSectionStrategy(ButtonsOfMenu buttons) {
        super(FOOD_ITEM);
        this.buttons = buttons;
    }

    @Override
    public SendMessage apply(long chatId) {
        return getSendMessage(chatId, buttons.foodSectionMenu());
    }
}
