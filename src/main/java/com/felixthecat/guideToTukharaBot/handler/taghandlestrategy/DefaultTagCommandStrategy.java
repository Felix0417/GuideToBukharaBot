package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.ArticleDataRepository;
import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultTagCommandStrategy implements TagCommandStrategy {

    private final ArticleDataRepository articleDataRepository;

    @Override
    public Tags getKey() {
        return null;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();
        val messageText = update.getCallbackQuery().getData();

        val tag = Tags.valueOf(messageText);

        var result = new ArrayList<BotApiMethod>();
        var data = new SendMessage();
        data.setChatId(String.valueOf(chatId));
        data.setText(articleDataRepository.getArticleDataById(tag.getDescription()).getData());
        result.add(data);

        String url = articleDataRepository.getArticleDataByAddress(tag.getDescription()).getAddress();
        if (url != null) {
            var buttons = new ArrayList<List<InlineKeyboardButton>>();
            buttons.add(List.of(
                    getInlineKeyboardButton(MenuButtonTags.URL_GET_BUTTON, url),
                    getInlineKeyboardButton(MenuButtonTags.URL_BACK_TO_MAIN_MENU, null)));

            var additionalData = messageForDrawingButtons(chatId, MenuButtonTags.URL_TEXT);
            additionalData.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));
            result.add(additionalData);
        }

        return result;
    }

    private InlineKeyboardButton getInlineKeyboardButton(MenuButtonTags tag, String text) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(tag.getDescription());
        if (null != text) {
            inlineKeyboardButton.setUrl(text);
        } else {
            inlineKeyboardButton.setCallbackData(tag.getCommand());
        }
        return inlineKeyboardButton;
    }

    protected SendMessage messageForDrawingButtons(long chatId, MenuButtonTags tagFirst) {
        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(tagFirst.getDescription());
        return message;
    }

    protected InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }
}
