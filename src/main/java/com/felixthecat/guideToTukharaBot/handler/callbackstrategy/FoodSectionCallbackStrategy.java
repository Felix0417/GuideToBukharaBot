package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class FoodSectionCallbackStrategy extends AbstractCallbackStrategy {

    public FoodSectionCallbackStrategy(ArticleDataRepository articleDataRepository, CallbacksRepository callbacksRepository, UserRepository userRepository) {
        super(articleDataRepository, callbacksRepository, userRepository);
    }

    @Override
    public String getKey() {
        return "Раздел еда";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = update.getCallbackQuery().getMessage();
        return getNewMenuMessage(message, getKey(), getButtonsListForMenu(getKey()));
    }
}
