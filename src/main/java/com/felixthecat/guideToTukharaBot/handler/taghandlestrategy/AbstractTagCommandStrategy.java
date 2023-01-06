package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.felixthecat.guideToTukharaBot.entity.User;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.Tags.TOURIST_CHOICE;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class AbstractTagCommandStrategy implements TagCommandStrategy{

    private final UserRepository repository;

    protected SendMessage getSendMessage(long chatId, List<MenuButtonTags> list) {
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

    protected InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    protected List<BotApiMethod> getUpdatedBotApiMethodList(Update update, Tags tags){
        val message = update.getMessage();
        val messageId = update.getCallbackQuery().getMessage().getMessageId();
        val chatId = update.getCallbackQuery().getFrom().getId();
        User user = repository.findById(chatId).get();
        user.setStatus(tags.getDescription());
        repository.save(user);
        log.info("User has update settings: " + user);
        return List.of(getEditMessage(chatId,messageId));
    }

    private EditMessageText getEditMessage(long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(MenuButtonTags.STATUS_HAS_CHANGED.getDescription());
        editMessage.setMessageId((int) messageId);
        return editMessage;
    }
}
