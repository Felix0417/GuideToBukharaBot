package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.telegramBots.GuideToBukharaBot.model.Tags.FOOD_ITEM;

@Slf4j
@Component
@RequiredArgsConstructor
public class FoodTagCommandStrategy implements TagCommandStrategy {

    private final ButtonsOfMenu buttons;

    @Override
    public Tags getKey() {
        return FOOD_ITEM;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();
        return List.of(getSendMessage(chatId, buttons.foodSectionMenu()));
    }

    private SendMessage getSendMessage(long chatId, List<MenuButtonTags> list) {
        var buttons = StreamEx.of(list)
                .skip(1L)
                .map(this::getButtonList).toList();

        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(list.get(0).getDescription());
        message.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));
        return message;
    }

    private List<InlineKeyboardButton> getButtonList(MenuButtonTags tag) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(tag.getDescription());
        inlineKeyboardButton.setCallbackData(tag.toString());
        return List.of(inlineKeyboardButton);
    }

    private InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
