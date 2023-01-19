package com.felixthecat.guideToTukharaBot.handler.callbackstrategy.endpointscallback;

import com.felixthecat.guideToTukharaBot.handler.callbackstrategy.AbstractCallbackStrategy;
import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class NationalKitchenEndpointCallbackStrategy extends AbstractCallbackStrategy {

    public NationalKitchenEndpointCallbackStrategy(ArticleDataRepository articleDataRepository, CallbacksRepository callbacksRepository, UserRepository userRepository) {
        super(articleDataRepository, callbacksRepository, userRepository);
    }

    @Override
    public String getKey() {
        return "Рестораны_национальной_кухни";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        return getEndlessMessage(update, getKey());
    }
}
