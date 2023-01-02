package com.felixthecat.guideToTukharaBot.bot;

import com.felixthecat.guideToTukharaBot.dispatcher.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

@RequiredArgsConstructor
public class TelegramLongPollingBotService extends TelegramLongPollingBot implements LongPollingBot {
    private final BotConfig config;
    private final MessageDispatcher dispatcher;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        StreamEx.of(dispatcher.dispatch(update))
                .forEach(this::exec);
    }

    @SneakyThrows
    private void exec(BotApiMethod method) {
        execute(method);
    }
}
