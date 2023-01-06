package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
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
public class DefaultTagCommandStrategy extends AbstractTagCommandStrategy{

    private final ArticleDataRepository articleDataRepository;

    public DefaultTagCommandStrategy(UserRepository repository, ArticleDataRepository articleDataRepository) {
        super(repository);
        this.articleDataRepository = articleDataRepository;
    }

    @Override
    public Tags getKey() {
        return null;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = update.getCallbackQuery().getFrom().getId();
        val messageText = update.getCallbackQuery().getData();

        val menuButtonTag = MenuButtonTags.valueOf(messageText);

        var result = new ArrayList<BotApiMethod>();
        var data = new SendMessage();
        data.setChatId(String.valueOf(chatId));
        data.setText(articleDataRepository.getArticleDataById(menuButtonTag.getCommand()).getData());
        result.add(data);

        String url = articleDataRepository.getArticleDataByAddress(menuButtonTag.getCommand()).getAddress();

        if (url != null) {
            var buttons = new ArrayList<List<InlineKeyboardButton>>();
            buttons.add(List.of(
                    getInlineKeyboardButton(MenuButtonTags.URL_GET_BUTTON, url)));
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


}
