package com.felixthecat.guideToTukharaBot.handler;

import com.felixthecat.guideToTukharaBot.handler.taghandlestrategy.TagCommandStrategy;
import com.felixthecat.guideToTukharaBot.model.Tags;
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
public class CallbackMessageHandler implements MessageHandler {

    private final Map<Tags, TagCommandStrategy> tagsTagCommandStrategyMap;

    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val text = update.getCallbackQuery().getData();

        return StreamEx.of(Tags.values())
                .map(Enum::toString)
                .filter(Objects::nonNull)
                .findAny(text::equalsIgnoreCase)
                .map(Tags::valueOf)
                .map(tagsTagCommandStrategyMap::get)
                .orElse(tagsTagCommandStrategyMap.get(null))
                .handle(update);
    }
}
