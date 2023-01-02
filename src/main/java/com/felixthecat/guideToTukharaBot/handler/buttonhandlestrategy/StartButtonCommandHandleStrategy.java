package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import com.telegramBots.GuideToBukharaBot.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.START;

@Component
@RequiredArgsConstructor
public class StartButtonCommandHandleStrategy implements ButtonCommandStrategy {

    private final ButtonsOfMenu buttons;
    private final UserRegistrationService userRegistrationService;

    @Override
    public MenuButtonTags getKey() {
        return START;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();

        var result = new ArrayList<BotApiMethod>();
        result.add(prepareMessage(chatId, buttons.startCommand(update)));
        if (!buttons.containsUserInRepository(chatId)) {
            userRegistrationService.register(update);
            result.add(drawingButtons(chatId, buttons.changeUserStatus()));
        }
        result.add(drawingButtons(chatId, buttons.startMainMenu(chatId)));

        return result;
    }

    private SendMessage drawingButtons(long chatId, List<MenuButtonTags> tags) {
        var buttons = StreamEx.of(tags)
                .skip(1L)
                .map(this::getButtonList)
                .toList();

        var message = messageForDrawingButtons(chatId, tags.get(0));
        message.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));

        return message;
    }

    private SendMessage prepareMessage(long chatId, String text) {
        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }

    private List<InlineKeyboardButton> getButtonList(MenuButtonTags tag) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(tag.getCommand());
        inlineKeyboardButton.setText(tag.getDescription());
        return List.of(inlineKeyboardButton);
    }

    private SendMessage messageForDrawingButtons(long chatId, MenuButtonTags tagFirst) {
        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(tagFirst.getDescription());
        return message;
    }

    private InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
