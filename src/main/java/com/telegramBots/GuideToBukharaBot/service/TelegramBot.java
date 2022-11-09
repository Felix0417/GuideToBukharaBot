package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.config.BotConfig;
import com.telegramBots.GuideToBukharaBot.model.User;
import com.telegramBots.GuideToBukharaBot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot implements LongPollingBot {

    @Autowired
    private UserRepository userRepository;

    private final BotConfig config;

    private final String USER_STATUS_TOURIST = "TOURIST";
    private final String USER_STATUS_LOCAL = "LOCAL";

    private final String ATTRACTIONS_OF_CITY = "ATTRACTIONS";
    private final String FOOD_OF_CITY = "FOOD";
    private final String HOTELS = "HOTEL";

    final static String HELP_TEXT = "Список комманд \n " +
            "- /start используется для перезапуска бота  \n" +
            " - /my_data отображает ваши персональные данные(имя, фамилия и id \n" +
            " - /about отображает создателя бота и права на него";

    final static String USER_DATA = "Вот ваши данные: \n\n " +
            "Ваш ID - %d \n\n" +
            "Ваше имя пользователя - %s \n\n" +
            "Дата первого общения с этим ботом - \n %s";

    public TelegramBot(BotConfig config) {
        this.config = config;


        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Перезапустить бота"));
        listOfCommands.add(new BotCommand("/my_data", "Ваши данные"));
        listOfCommands.add(new BotCommand("/help", "О коммандах"));
        listOfCommands.add(new BotCommand("/about", "О боте"));
        listOfCommands.add(new BotCommand("/settings", "Настройки пользователя"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), "ru"));
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
                    registeredUser(update, false);
                    break;
                case "/my_data":
                    Optional<User> user = userRepository.findById(update.getMessage().getChat().getId());
                    String regDate = new SimpleDateFormat("d.MM.yyyy hh:mm").format(user.get().getRegisteredAt());
                    sendMessage(chatId, String.format(USER_DATA, user.get().getChatId(), user.get().getUserName(), regDate));
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/about":

                    break;
                case "/settings":
                    changeYourStatus(update);
                    break;
                default:
                    sendMessage(chatId, "Извините, команда не распознана, как насчет " +
                            "воспользоваться кнопками или кнопкой меню в поле ввода?");
            }
        } else if (update.hasCallbackQuery()) {
            String textOfMessage = update.getCallbackQuery().getData();
            if (textOfMessage.equals(USER_STATUS_LOCAL) || textOfMessage.equals(USER_STATUS_TOURIST)) {
                registerUserStatus(update.getCallbackQuery().getData(), update.getCallbackQuery().getMessage().getChatId());
            }
        }
    }

    private void registeredUser(Update update, boolean setStatus) {
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
            changeYourStatus(update);
        }
    }

    private void changeYourStatus(Update update){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Выберите ваш статус");

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inLineButtons = new ArrayList<>();
        List<InlineKeyboardButton> changeButtons = new ArrayList<>();
        var asTourist = new InlineKeyboardButton();
        asTourist.setText("Турист");
        asTourist.setCallbackData(USER_STATUS_TOURIST);

        var asLocal = new InlineKeyboardButton();
        asLocal.setText("Местный / Релокант");
        asLocal.setCallbackData(USER_STATUS_LOCAL);

        changeButtons.add(asTourist);
        changeButtons.add(asLocal);

        inLineButtons.add(changeButtons);
        keyboardMarkup.setKeyboard(inLineButtons);

        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void registerUserStatus(String data, long chatId){
        User user = userRepository.findById(chatId).get();
        user.setStatus(statusUpdate(data));
        userRepository.save(user);
        log.info("User has update settings: " + user);

        String status = "Ваш статус успешно изменен!";
        sendMessage(chatId, status);

        startMainMenu(chatId);
    }

    private String statusUpdate(String  message){
            return USER_STATUS_TOURIST.equals(message)? USER_STATUS_TOURIST : USER_STATUS_LOCAL;
    }

    private void startMainMenu(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Какие данные вам интересны?");

        InlineKeyboardMarkup mainMenuMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> mainMenuButtons = new ArrayList<>();

        InlineKeyboardButton placeAttractions = new InlineKeyboardButton();
        placeAttractions.setText("Достопримечательности");
        placeAttractions.setCallbackData(ATTRACTIONS_OF_CITY);

        InlineKeyboardButton restaurants = new InlineKeyboardButton();
        restaurants.setText("Где поесть");
        restaurants.setCallbackData(FOOD_OF_CITY);

        InlineKeyboardButton hotels = new InlineKeyboardButton();
        hotels.setText("Гостиницы");
        hotels.setCallbackData(HOTELS);

        mainMenuButtons.add(List.of(placeAttractions));
        mainMenuButtons.add(List.of(restaurants));
        mainMenuButtons.add((List.of(hotels)));
        mainMenuMarkup.setKeyboard(mainMenuButtons);

        message.setReplyMarkup(mainMenuMarkup);
        executeMessage(message);

    }

    private void startCommandReceived(long chatId, String name) {
        String answer = String.format("Привет, %s, рад тебя видеть", name);
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(" Error occurred " + e.getMessage());
        }
    }
}
