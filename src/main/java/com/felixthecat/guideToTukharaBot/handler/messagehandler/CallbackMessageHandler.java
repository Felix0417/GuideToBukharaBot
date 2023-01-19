package com.felixthecat.guideToTukharaBot.handler.messagehandler;

import com.felixthecat.guideToTukharaBot.handler.callbackstrategy.CallbackStrategy;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CallbackMessageHandler implements MessageHandler {

    @Resource(name = "getCallbackStrategyMap")
    private final Map<String, CallbackStrategy> callbackStrategyMap;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val text = update.getCallbackQuery().getData();
        return callbackStrategyMap
                .getOrDefault(text, callbackStrategyMap.get(null))
                .handler(update);
    }
}
