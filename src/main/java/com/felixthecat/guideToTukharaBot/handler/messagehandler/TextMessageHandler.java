package com.felixthecat.guideToTukharaBot.handler.messagehandler;

import com.felixthecat.guideToTukharaBot.handler.callbackstrategy.CallbackStrategy;
import com.felixthecat.guideToTukharaBot.handler.handlerstrategy.CommandStrategy;
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
public class TextMessageHandler implements MessageHandler {

    @Resource
    private final Map<String, CommandStrategy> commandStrategyMap;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val text = message.getText();
        return commandStrategyMap
                .getOrDefault(text,commandStrategyMap.get(null))
                .handler(update);
    }
}
