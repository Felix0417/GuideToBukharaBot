package com.felixthecat.guideToTukharaBot.handler.messagehandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
public class ErrorMessageHandler implements MessageHandler {
    @Override
    public boolean canHandle(Update update) {
        return false;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        log.error("No suitable message handlers found for update: {}." +
                "\n Aborting message processing.", update);
        return List.of();
    }
}
