package com.felixthecat.guideToTukharaBot.handler.callbackstrategy.endpointscallback;

import com.felixthecat.guideToTukharaBot.entity.Callbacks;
import com.felixthecat.guideToTukharaBot.handler.callbackstrategy.AbstractCallbackStrategy;
import com.felixthecat.guideToTukharaBot.handler.handlerstrategy.CommandStrategy;
import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InCityEndpointCallbackStrategy extends AbstractCallbackStrategy {

    private final ArticleDataRepository articleDataRepository;
    private final CallbacksRepository callbacksRepository;

    @Override
    public String getKey() {
        return "Достопримечательности_город";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = update.getCallbackQuery().getMessage();
        var articleData = articleDataRepository.getArticleDataByCommand(getKey());
        var backCallbacks = callbacksRepository.getCallbacksByCommand(getKey());
        return getNewMenuMessage(message, articleData.getData(), getEndlessCallbackList(articleData, backCallbacks));
    }
}
