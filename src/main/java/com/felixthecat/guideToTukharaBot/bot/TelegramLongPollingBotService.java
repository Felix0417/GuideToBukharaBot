package com.felixthecat.guideToTukharaBot.bot;

import com.felixthecat.guideToTukharaBot.dispatcher.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramLongPollingBotService extends TelegramLongPollingBot implements LongPollingBot {
    private final BotConfig config;
    private final MessageDispatcher dispatcher;
    private final List<BotCommand> leftMenuCommands;

    private final LeftMenuButtonsBeanConfiguration leftMenu;

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
        execute(new SetMyCommands(leftMenuCommands, new BotCommandScopeDefault(), "ru"));
    }

    @SneakyThrows
    private void exec(BotApiMethod method) {
        execute(method);
    }
}
