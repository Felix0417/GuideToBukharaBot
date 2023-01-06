package com.felixthecat.guideToTukharaBot.dispatcher;

import com.felixthecat.guideToTukharaBot.handler.messagehandler.ErrorMessageHandler;
import com.felixthecat.guideToTukharaBot.handler.messagehandler.MessageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageDispatcherImpl implements MessageDispatcher {

    private final List<MessageHandler> handlers;
    private final ErrorMessageHandler errorMessageHandler;

    public List<BotApiMethod> dispatch(Update update) {
        return StreamEx.of(handlers)
                .filter(h -> h.canHandle(update))
                .findFirst()
                .orElse(errorMessageHandler)
                .handle(update);
    }

}
