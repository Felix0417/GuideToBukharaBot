package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Slf4j
public class TouristChoiceCallbackStrategy extends AbstractCallbackStrategy{

    public TouristChoiceCallbackStrategy(ArticleDataRepository articleDataRepository, CallbacksRepository callbacksRepository, UserRepository userRepository) {
        super(articleDataRepository, callbacksRepository, userRepository);
    }

    @Override
    public String getKey() {
        return "Турист";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        return saveUserStatusAndGetMainMenu(update, getKey());
    }
}
