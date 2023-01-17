package com.felixthecat.guideToTukharaBot.handler.callbackstrategy.endpointscallback;

import com.felixthecat.guideToTukharaBot.handler.callbackstrategy.AbstractCallbackStrategy;
import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GuidesEndpointCallbackStrategy extends AbstractCallbackStrategy {

    private final ArticleDataRepository articleDataRepository;
    private final CallbacksRepository callbacksRepository;

    private static final String BACK_BUTTON_TEXT = "Назад";

    @Override
    public String getKey() {
        return "Гиды";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = update.getCallbackQuery().getMessage();
        var articleData = articleDataRepository.getArticleDataByCommand(getKey());
        var backCallbacks = callbacksRepository.getCallbacksByCommand(getKey());
        backCallbacks.setDescription(BACK_BUTTON_TEXT);
        return getNewMenuMessage(message, articleData.getData(), List.of(backCallbacks));
    }
}
