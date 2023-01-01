package com.telegramBots.GuideToBukharaBot.service.strategies;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.function.Function;

public interface ButtonStrategy {

    SendMessage apply(long chatId);

    MenuButtonTags getKey();

    default InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    default List<InlineKeyboardButton> getButtonList(MenuButtonTags tag) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(tag.getCommand());
        inlineKeyboardButton.setText(tag.getDescription());
        return List.of(inlineKeyboardButton);
    }
}
