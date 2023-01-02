package com.telegramBots.GuideToBukharaBot.service.strategies.userStatusStrategies;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import one.util.streamex.StreamEx;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface UserStatusStrategy {
    EditMessageText apply(long chatId, long messageId);

    Tags getKey();

    default EditMessageText editMessage(long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(MenuButtonTags.STATUS_HAS_CHANGED.getDescription());
        editMessage.setMessageId((int) messageId);
        return editMessage;
    }

    default SendMessage getSendMessage(long chatId, List<MenuButtonTags> list) {
        var buttons = StreamEx.of(list)
                .skip(1L)
                .map(this::getButtonList).toList();

        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(list.get(0).getDescription());
        message.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));
        return message;
    }

    default InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    default List<InlineKeyboardButton> getButtonList(MenuButtonTags tag) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(tag.getDescription());
        inlineKeyboardButton.setCallbackData(tag.toString());
        return List.of(inlineKeyboardButton);
    }
}
