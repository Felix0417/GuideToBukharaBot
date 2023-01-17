package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import com.felixthecat.guideToTukharaBot.entity.ArticleData;
import com.felixthecat.guideToTukharaBot.entity.Callbacks;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public abstract class AbstractCallbackStrategy implements CallbackStrategy {

    private static final String GET_URL_BUTTON_TEXT = "Узнать подробнее";
    private static final String BACK_BUTTON_TEXT = "Назад";

    public List<BotApiMethod> getNewMenuMessage(Message message, String text, List<Callbacks> callbacksList) {
        var newMessage = new SendMessage();
        newMessage.setChatId(String.valueOf(message.getChatId()));
        newMessage.setText(text);
        newMessage.setReplyMarkup(getInlineReplyMarkup(callbacksList));
        return List.of(getDeleteMessage(message), newMessage);
    }


    protected InlineKeyboardMarkup getInlineReplyMarkup(List<Callbacks> buttons) {
        var newButtons = StreamEx.of(buttons)
                .map(this::getInlineKeyboardButton)
                .toList();
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(newButtons);
        return keyboardMarkup;
    }

    protected List<InlineKeyboardButton> getInlineKeyboardButton(Callbacks button) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(button.getDescription());
        var command = button.getCommand();
        if (command.matches("(https://).+")) {
            inlineKeyboardButton.setUrl(command);
        } else {
            inlineKeyboardButton.setCallbackData(button.getCommand());
        }
        return List.of(inlineKeyboardButton);
    }

    protected List<Callbacks> getEndlessCallbackList(ArticleData articleData, Callbacks callbacks){
        var buttonWithUrl = new Callbacks();
        buttonWithUrl.setDescription(GET_URL_BUTTON_TEXT);
        buttonWithUrl.setCommand(articleData.getAddress());

        callbacks.setDescription(BACK_BUTTON_TEXT);
        return List.of(buttonWithUrl, callbacks);
    }

    protected BotApiMethod getDeleteMessage(Message message) {
        var deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        return deleteMessage;
    }
}
