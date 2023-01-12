package com.felixthecat.guideToTukharaBot.handler.handlerstrategy;

import com.felixthecat.guideToTukharaBot.entity.Callbacks;
import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public abstract class AbstractCommandStrategy implements CommandStrategy {

    private final StartCommandsRepository startCommandsRepository;

    public List<BotApiMethod> getNewMessage(long chatId, String command, List<Callbacks> callbacksList) {
        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(startCommandsRepository.getByCommand(command).getStartMessageText());
        if (callbacksList != null) {
            message.setReplyMarkup(getInlineReplyMarkup(callbacksList));
        }
        return List.of(message);
    }

    public InlineKeyboardMarkup getInlineReplyMarkup(List<Callbacks> callbacksList) {
        var buttons = StreamEx.of(callbacksList)
                .map(this::getButton)
                .toList();
        return new InlineKeyboardMarkup(buttons);
    }

    public List<InlineKeyboardButton> getButton(Callbacks callbacks) {
        var button = new InlineKeyboardButton();
        button.setText(callbacks.getDescription());
        button.setCallbackData(callbacks.getCommand());
        return List.of(button);
    }
}