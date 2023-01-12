package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;


import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LocalChoiceCallbackStrategy extends AbstractCallbackStrategy {

    private final UserRepository userRepository;
    private final CallbacksRepository callbacksRepository;

    private static final String MAIN_MENU_TEXT = "Какие данные вам интересны?";

    @Override
    public String getKey() {
        return "Местный";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = update.getCallbackQuery().getMessage();
        var user = userRepository.findById(message.getChatId()).get();
        user.setStatus(getKey());
        userRepository.save(user);
        log.info(String.format("User %d has change his status - %s", message.getChatId(), getKey()));
        var callbacks = callbacksRepository.getAllByUserStatus(getKey());
        return getNewMenuMessage(message, MAIN_MENU_TEXT,callbacks);
    }
}
