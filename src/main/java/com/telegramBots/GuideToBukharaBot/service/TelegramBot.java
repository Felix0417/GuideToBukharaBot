package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.config.BotConfig;
import com.telegramBots.GuideToBukharaBot.model.User;
import com.telegramBots.GuideToBukharaBot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButton;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot implements LongPollingBot {

    @Autowired
    private UserRepository userRepository;

    private final BotConfig config;

    final static String HELP_TEXT = "Список комманд \n - /start используется для перезапуска бота  \n" +
            " - /my_data отображает ваши персональные данные(имя, фамилия и id \n" +
            " - /about отображает создателя бота и права на него";

    public TelegramBot(BotConfig config) {
        this.config = config;


        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "rerun bot"));
        listOfCommands.add(new BotCommand("/my_data", "yours data"));
        listOfCommands.add(new BotCommand("/help", "about commands"));
        listOfCommands.add(new BotCommand("/about", "about bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting's bot command list: " + e.getMessage());
        }
    }

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    registeredUser(update);
                    break;
                case "/my_data":

                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/about":

                    break;
                default:
                    sendMessage(chatId, "Sorry, but command is not recognized");
            }
        }
    }

    private void registeredUser(Update update) {
        Message message = update.getMessage();
        var chatId = message.getChatId();
        if (userRepository.findById(chatId).isEmpty()) {

            var chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("User saved: " + user);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = String.format("Hi, %s, nice to meet you", name);
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(" Error occurred " + e.getMessage());
        }
    }
}
