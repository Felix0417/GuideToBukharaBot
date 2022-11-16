package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.config.BotConfig;
import com.telegramBots.GuideToBukharaBot.model.*;
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

    @Autowired
    private ArticleDataRepository articleDataRepository;

    private final BotConfig config;

    Tags tags = null;

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
        long chatId;
        String messageText;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    registeredUser(update);
                    break;
                case "/my_data":
                    Optional<User> user = userRepository.findById(update.getMessage().getChat().getId());
                    String regDate = new SimpleDateFormat("d.MM.yyyy hh:mm").format(user.get().getRegisteredAt());
                    sendMessage(chatId, String.format(
                            articleDataRepository.getArticleDataById(Tags.USER_DATA.getDescription()).getData(),
                            user.get().getChatId(), user.get().getUserName(), regDate));
                    break;
                case "/help":
                    sendMessage(chatId, articleDataRepository.getArticleDataById(Tags.HELP.getDescription()).getData());
                    break;
                case "/about":
                    sendMessage(chatId, articleDataRepository.getArticleDataById(Tags.ABOUT_BOT.getDescription()).getData());
                    break;
                case "/settings":
                    changeUserStatus(update);
                    break;
                default:
                    if (messageText.contains("/addArticle") && chatId == config.getOwner()){
                        addDataToArticleRepository(chatId, messageText.replaceFirst("/addArticle ",""));
                    }else {
                        wrongRequestFromUser(chatId);
                    }
            }

        } else if (update.hasCallbackQuery()) {
            messageText = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getFrom().getId();
            if (Arrays.stream(Tags.values()).map(Tags::toString).anyMatch(x -> x.equalsIgnoreCase(messageText))){
                tags = Tags.valueOf(messageText);
                switch (tags){
                    case TOURIST_CHOICE:
                    case LOCAL_CHOICE:
                        registerUserStatus(tags.getDescription(),chatId);
                        break;
                    case ATTRACTIONS_ITEM:
                        attractionSectionMenu(chatId);
                        break;
                    case FOOD_ITEM:
                        foodSectionMenu(chatId);
                        break;
                    case HOTELS_ITEM:
                        hotelsSectionMenu(chatId);
                        break;
                    default:
                        sendMessage(chatId, articleDataRepository.getArticleDataById(tags.getDescription()).getData());
                }
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
            changeUserStatus(update);
        }
    }

    private void changeUserStatus(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Выберите ваш статус");

        List<List<InlineKeyboardButton>> inLineButtons =
                new ArrayList<>(List.of(List.of(inlineKeyboardButtons("Турист", Tags.TOURIST_CHOICE.toString())),
                        List.of(inlineKeyboardButtons("Местный / Релокант", Tags.LOCAL_CHOICE.toString()))));

        updateKeyboardMarkup(message, inLineButtons);
    }

    private void registerUserStatus(String data, long chatId) {
        User user = userRepository.findById(chatId).get();
        user.setStatus(data);
        userRepository.save(user);
        log.info("User has update settings: " + user);

        String status = "Ваш статус успешно изменен!";
        sendMessage(chatId, status);

        startMainMenu(chatId);
    }

    private void startMainMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Какие данные вам интересны?");

        List<List<InlineKeyboardButton>> mainMenuLines = new ArrayList<>();

        mainMenuLines.add(List.of(inlineKeyboardButtons("Достопримечательности", Tags.ATTRACTIONS_ITEM.toString())));
        mainMenuLines.add(List.of(inlineKeyboardButtons("Где поесть", Tags.FOOD_ITEM.toString())));
        mainMenuLines.add(List.of(inlineKeyboardButtons("Гостиницы", Tags.HOTELS_ITEM.toString())));

        updateKeyboardMarkup(message, mainMenuLines);
    }

    public void attractionSectionMenu(long chatId){
        SendMessage attractionsMessage = new SendMessage();
        attractionsMessage.setChatId(String.valueOf(chatId));
        attractionsMessage.setText("Выберите локацию:");

        List<List<InlineKeyboardButton>> attractionLines = new ArrayList<>();

        attractionLines.add(List.of(inlineKeyboardButtons("В Городе", Tags.ATTRACTIONS_INSIDE_CITY.toString())));
        attractionLines.add(List.of(inlineKeyboardButtons("За Городом", Tags.ATTRACTIONS_OUTSIDE_CITY.toString())));
        attractionLines.add(List.of(inlineKeyboardButtons("Гиды", Tags.CONTACT_GUIDES.toString())));

        updateKeyboardMarkup(attractionsMessage, attractionLines);
    }

    public void foodSectionMenu(long chatId){
        SendMessage foodChangeMessage = new SendMessage();
        foodChangeMessage.setChatId(String.valueOf(chatId));
        foodChangeMessage.setText("Выберите интересующую вас кухню:");

        List<List<InlineKeyboardButton>> foodLine = new ArrayList<>();

        foodLine.add(List.of(inlineKeyboardButtons("Национальная кухня",Tags.NATIONAL_KITCHEN.toString())));
        foodLine.add(List.of(inlineKeyboardButtons("Европейская кухня", Tags.EUROPEAN_KITCHEN.toString())));
        foodLine.add(List.of(inlineKeyboardButtons("Азиатская кухня", Tags.ASIAN_KITCHEN.toString())));

        updateKeyboardMarkup(foodChangeMessage, foodLine);
    }

    public void hotelsSectionMenu(long chatId){
        SendMessage hotelsMessage = new SendMessage();
        hotelsMessage.setChatId(String.valueOf(chatId));
        hotelsMessage.setText("Выберите тип отеля:");

        List<List<InlineKeyboardButton>> hotelsLine = new ArrayList<>();

        hotelsLine.add(List.of(inlineKeyboardButtons("Гостиница / Отель", Tags.HOTELS.toString())));
        hotelsLine.add(List.of(inlineKeyboardButtons("Гостевые дома", Tags.GUEST_HOUSES.toString())));

        updateKeyboardMarkup(hotelsMessage, hotelsLine);
    }

    public void updateKeyboardMarkup(SendMessage message, List<List<InlineKeyboardButton>> buttons){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);

        message.setReplyMarkup(inlineKeyboardMarkup);
        executeMessage(message);
    }

    public InlineKeyboardButton inlineKeyboardButtons(String text, String callbackData){
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = String.format("Привет, %s, рад тебя видеть! " +
                "Вот наши разделы:", name);
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
        startMainMenu(chatId);
    }

    public void addDataToArticleRepository(long chatId, String message){
        List<String> data = List.of(message.split("#\n"));
        ArticleData newArticle = new ArticleData();
        newArticle.setId(data.get(0));
        newArticle.setData(data.get(1));
        articleDataRepository.save(newArticle);

        sendMessage(chatId, "Данные успешно добавлены");
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(" Error occurred " + e.getMessage());
        }
    }

    void wrongRequestFromUser(long chatId){
        sendMessage(chatId, "Извините, команда не распознана, как насчет " +
                "воспользоваться кнопкой меню в поле ввода?");
    }
}
