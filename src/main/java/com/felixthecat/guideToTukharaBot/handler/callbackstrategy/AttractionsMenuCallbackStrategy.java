package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttractionsMenuCallbackStrategy extends AbstractCallbackStrategy {

    private final CallbacksRepository callbacksRepository;

    @Override
    public String getKey() {
        return "Достопримечательности";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = update.getCallbackQuery().getMessage();
        var attractions = callbacksRepository.getAllByButtonType(getKey());
        return getNewMenuMessage(message, getKey(), attractions);
    }
}
