package com.felixthecat.guideToTukharaBot.bot;

import com.felixthecat.guideToTukharaBot.dispatcher.MessageDispatcher;
import com.felixthecat.guideToTukharaBot.handler.LoadLeftMenu;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class TelegramLongPollingBotService extends TelegramLongPollingBot implements LongPollingBot {
    private final BotConfig config;
    private final MessageDispatcher dispatcher;

    private final LoadLeftMenu leftMenu;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        StreamEx.of(dispatcher.dispatch(update))
                .forEach(this::exec);
    }

    @SneakyThrows
    @PostConstruct
    private void initLeftMenu() {
        execute(new SetMyCommands(leftMenu.getListOfCommands(), new BotCommandScopeDefault(), "ru"));
    }

    @SneakyThrows
    private void exec(BotApiMethod method) {
        execute(method);
    }
}
