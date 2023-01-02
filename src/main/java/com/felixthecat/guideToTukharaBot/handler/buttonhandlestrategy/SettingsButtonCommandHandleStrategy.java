package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.felixthecat.guideToTukharaBot.handler.ButtonsOfMenu;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import lombok.RequiredArgsConstructor;
import lombok.val;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.MenuButtonTags.SETTINGS;

@Component
@RequiredArgsConstructor
public class SettingsButtonCommandHandleStrategy implements ButtonCommandStrategy {

    private final ButtonsOfMenu buttons;

    @Override
    public MenuButtonTags getKey() {
        return SETTINGS;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();

        var tags = buttons.changeUserStatus();
        var buttons = StreamEx.of(this.buttons.changeUserStatus())
                .skip(1L)
                .map(this::getButtonList).toList();

        var result = new SendMessage();
        result.setText(tags.get(0).getDescription());
        result.setChatId(String.valueOf(chatId));
        result.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));
        return List.of(result);
    }

    private List<InlineKeyboardButton> getButtonList(MenuButtonTags tag) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(tag.getCommand());
        inlineKeyboardButton.setText(tag.getDescription());
        return List.of(inlineKeyboardButton);
    }

    private InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
