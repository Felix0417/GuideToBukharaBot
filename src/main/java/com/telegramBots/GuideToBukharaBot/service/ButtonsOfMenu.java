package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.entity.ArticleData;
import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class ButtonsOfMenu{

    TelegramBot bot;

    public void drawingTitleMenu(){
//        List<BotCommand> listOfCommands = new ArrayList<>();
//        listOfCommands.add(new BotCommand(MenuButtonTags.START.getCommand(), MenuButtonTags.START.getDescription()));
//        listOfCommands.add(new BotCommand(MenuButtonTags.USER_DATA.getCommand(), MenuButtonTags.USER_DATA.getDescription()));
//        listOfCommands.add(new BotCommand(MenuButtonTags.HELP.getCommand(), MenuButtonTags.HELP.getDescription()));
//        listOfCommands.add(new BotCommand(MenuButtonTags.ABOUT_BOT.getCommand(), MenuButtonTags.ABOUT_BOT.getDescription()));
//        listOfCommands.add(new BotCommand(MenuButtonTags.SETTINGS.getCommand(), MenuButtonTags.SETTINGS.getDescription()));
//        try {
//            bot.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), "ru"));
//        } catch (TelegramApiException e) {
//            log.error("Error setting's bot command list: " + e.getMessage());
//        }
    }

    protected void startCommand(Update update) {
        var chatId = update.getMessage().getChatId();
        var chat = update.getMessage().getChat();
        if (bot.getUserRepository().findById(chatId).isEmpty()) {
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            bot.getUserRepository().save(user);
            log.info("User saved: " + user);
            changeUserStatus(update);
        }
        String answer = String.format(MenuButtonTags.GREETINGS_TEXT.getDescription(), chat.getUserName());
        log.info("Replied to user " + chat.getUserName());
        sendMessage(chatId, answer);
        startMainMenu(chatId);
    }

    protected void userDataCommand(long chatId){
        Optional<User> user = bot.getUserRepository().findById(chatId);
        String regDate = new SimpleDateFormat("d.MM.yyyy hh:mm").format(user.get().getRegisteredAt());
        sendMessage(chatId, String.format(
                bot.getArticleDataRepository().getArticleDataById(Tags.USER_DATA.getDescription()).getData(),
                user.get().getChatId(),
                user.get().getUserName(),
                regDate));
    }

    protected void helpCommand(long chatId){
        sendMessage(chatId, bot.getArticleDataRepository().getArticleDataById(Tags.HELP.getDescription()).getData());
    }

    protected void aboutBotCommand(long chatId){
        sendMessage(chatId, bot.getArticleDataRepository().getArticleDataById(Tags.ABOUT_BOT.getDescription()).getData());
    }

    protected void changeUserStatus(Update update) {
        drawingButtons(update.getMessage().getChatId(), List.of(
                MenuButtonTags.STATUS_TEXT,
                MenuButtonTags.STATUS_TOURIST,
                MenuButtonTags.STATUS_LOCAL
        ));
    }

    protected void registerUserStatus(String data, long chatId) {
        User user = bot.getUserRepository().findById(chatId).get();
        user.setStatus(data);
        bot.getUserRepository().save(user);
        log.info("User has update settings: " + user);
        sendMessage(chatId, MenuButtonTags.STATUS_HAS_CHANGED.getDescription());
        startMainMenu(chatId);
    }

    private void startMainMenu(long chatId) {
        List<MenuButtonTags> mainMenuTags = new ArrayList<>(){};
        if (bot.getUserRepository().findById(chatId).get().getStatus().equals(Tags.TOURIST_CHOICE.getDescription())){
            mainMenuTags.addAll(List.of(
                    MenuButtonTags.MAIN_MENU_TEXT,
                    MenuButtonTags.ATTRACTIONS,
                    MenuButtonTags.FOOD,
                    MenuButtonTags.HOTELS));
        }else if (bot.getUserRepository().findById(chatId).get().getStatus().equals(Tags.LOCAL_CHOICE.getDescription())) {
            mainMenuTags.addAll(List.of(
                    MenuButtonTags.MAIN_MENU_TEXT,
                    MenuButtonTags.FOOD,
                    MenuButtonTags.FLATS_FOR_RENT_EXTENDED_ACCESS,
                    MenuButtonTags.FEATURES_OF_LIFE_EXTENDED_ACCESS,
                    MenuButtonTags.FOOD_PRICES_EXTENDED_ACCESS
                    ));
        }
        drawingButtons(chatId, mainMenuTags);
    }

    protected void attractionSectionMenu(long chatId){
        drawingButtons(chatId, List.of(
                MenuButtonTags.ATTRACTIONS_MENU_TEXT,
                MenuButtonTags.INSIDE_CITY,
                MenuButtonTags.OUTSIDE_CITY,
                MenuButtonTags.GUIDES
        ));
    }

    protected void foodSectionMenu(long chatId){
        drawingButtons(chatId, List.of(
                MenuButtonTags.FOOD_SECTION_TEXT,
                MenuButtonTags.NATIONAL_KITCHEN,
                MenuButtonTags.EUROPEAN_KITCHEN,
                MenuButtonTags.ASIAN_KITCHEN
        ));
    }

    protected void hotelsSectionMenu(long chatId){
        drawingButtons(chatId, List.of(
                MenuButtonTags.HOTELS_MENU_TEXT,
                MenuButtonTags.HOTEL,
                MenuButtonTags.GUEST_HOUSE
        ));
    }

    protected void drawingButtons(long chatId, List<MenuButtonTags> tags){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(tags.get(0).getDescription());

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (int i = 1; i < tags.size(); i++) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(tags.get(i).getDescription());
            inlineKeyboardButton.setCallbackData(tags.get(i).getCommand());

            buttons.add(List.of(inlineKeyboardButton));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);

        message.setReplyMarkup(inlineKeyboardMarkup);
        executeMessage(message);
    }

    void wrongRequestFromUser(long chatId){
        sendMessage(chatId, MenuButtonTags.WRONG_REQUEST_FROM_USER.getDescription());
    }

    public void addDataToArticleRepository(long chatId, String text){
        List<String> data = List.of(text.replaceFirst("/addArticle ", "").split("#\n"));
        ArticleData newArticle = new ArticleData();
        newArticle.setId(data.get(0));
        newArticle.setData(data.get(1));
        bot.getArticleDataRepository().save(newArticle);

        sendMessage(chatId, "Данные успешно добавлены");
    }

    private void executeMessage(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(" Error occurred " + e.getMessage());
        }
    }

    protected void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }
}