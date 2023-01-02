package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import com.telegramBots.GuideToBukharaBot.model.UserRepository;
import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.telegramBots.GuideToBukharaBot.model.Tags.LOCAL_CHOICE;
import static com.telegramBots.GuideToBukharaBot.model.Tags.TOURIST_CHOICE;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalTagCommandStrategy implements TagCommandStrategy {

    private final UserRepository repository;

    @Override
    public Tags getKey() {
        return LOCAL_CHOICE;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val messageId = message.getMessageId();
        val chatId = message.getChatId();
        User user = repository.findById(chatId).get();
        user.setStatus(TOURIST_CHOICE.getDescription());
        repository.save(user);
        log.info("User has update settings: " + user);

        return List.of(editMessage(chatId, messageId));
    }

    private EditMessageText editMessage(long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(MenuButtonTags.STATUS_HAS_CHANGED.getDescription());
        editMessage.setMessageId((int) messageId);
        return editMessage;
    }
}
