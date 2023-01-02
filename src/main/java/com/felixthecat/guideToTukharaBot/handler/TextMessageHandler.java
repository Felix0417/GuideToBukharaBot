package com.felixthecat.guideToTukharaBot.handler;

import com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy.ButtonCommandStrategy;
import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import lombok.RequiredArgsConstructor;
import lombok.val;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TextMessageHandler implements MessageHandler {
    private final Map<MenuButtonTags, ButtonCommandStrategy> commandStrategyMap;

    @Override
    public boolean canHandle(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val text = message.getText();

        return StreamEx.of(MenuButtonTags.values())
                .map(MenuButtonTags::getCommand)
                .filter(Objects::nonNull)
                .findAny(text::equals)
                .map(MenuButtonTags::valueOf)
                .map(commandStrategyMap::get)
                .orElse(commandStrategyMap.get(null))
                .handle(update);
    }
}
