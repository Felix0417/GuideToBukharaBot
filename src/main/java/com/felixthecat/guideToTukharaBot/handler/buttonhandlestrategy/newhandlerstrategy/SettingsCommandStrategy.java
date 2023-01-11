package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy.newhandlerstrategy;

import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class SettingsCommandStrategy extends AbstractCommandStrategy {

    private final CallbacksRepository callbacksRepository;

    public SettingsCommandStrategy(StartCommandsRepository startCommandsRepository
            , CallbacksRepository callbacksRepository) {
        super(startCommandsRepository);
        this.callbacksRepository = callbacksRepository;
    }

    public static String getStaticKey() {
        return "/settings";
    }

    @Override
    public String getKey() {
        return "/settings";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var callbackList = callbacksRepository.getAllByUserStatus("Выбор статуса");
        return getNewMessage(update.getMessage().getChatId(), getKey(), callbackList);
    }
}
