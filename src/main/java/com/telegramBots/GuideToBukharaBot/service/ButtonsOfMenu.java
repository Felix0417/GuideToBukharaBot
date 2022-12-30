package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
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
@RequiredArgsConstructor
public class ButtonsOfMenu {

    private final UserRepository userRepository;
    private final ArticleDataRepository articleDataRepository;

    protected void startCommand(Update update) {
        var chatId = update.getMessage().getChatId();
        var chat = update.getMessage().getChat();
        if (userRepository.findById(chatId).isEmpty()) {
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
        String answer = String.format(MenuButtonTags.GREETINGS_TEXT.getDescription(), chat.getUserName());
        log.info("Replied to user " + chat.getUserName());
        sendMessage(chatId, answer);
        startMainMenu(chatId);
    }

    protected void userDataCommand(long chatId){
        Optional<User> user = userRepository.findById(chatId);
        String regDate = new SimpleDateFormat("d.MM.yyyy hh:mm").format(user.get().getRegisteredAt());
        sendMessage(chatId, String.format(
                articleDataRepository.getArticleDataById(Tags.USER_DATA.getDescription()).getData(),
                user.get().getChatId(),
                user.get().getUserName(),
                regDate));
    }

    protected void helpCommand(long chatId){
        sendMessage(chatId, articleDataRepository.getArticleDataById(Tags.HELP.getDescription()).getData());
    }

    protected void aboutBotCommand(long chatId){
        sendMessage(chatId, articleDataRepository.getArticleDataById(Tags.ABOUT_BOT.getDescription()).getData());
    }

    protected void changeUserStatus(Update update) {
        drawingButtons(update.getMessage().getChatId(), List.of(
                MenuButtonTags.STATUS_TEXT,
                MenuButtonTags.STATUS_TOURIST,
                MenuButtonTags.STATUS_LOCAL
        ));
    }

    protected void registerUserStatus(String data, long chatId) {
        User user = userRepository.findById(chatId).get();
        user.setStatus(data);
        userRepository.save(user);
        log.info("User has update settings: " + user);
        startMainMenu(chatId);
    }

    protected void startMainMenu(long chatId) {
        List<MenuButtonTags> mainMenuTags = new ArrayList<>(){};
        if (userRepository.findById(chatId).get().getStatus().equals(Tags.TOURIST_CHOICE.getDescription())){
            mainMenuTags.addAll(List.of(
                    MenuButtonTags.MAIN_MENU_TEXT,
                    MenuButtonTags.ATTRACTIONS,
                    MenuButtonTags.FOOD,
                    MenuButtonTags.HOTELS));
        }else if (userRepository.findById(chatId).get().getStatus().equals(Tags.LOCAL_CHOICE.getDescription())) {
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

    protected void drawingUrlButton(long chatId, String url){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(MenuButtonTags.URL_TEXT.getDescription());

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        InlineKeyboardButton urlButton = new InlineKeyboardButton();
        urlButton.setText(MenuButtonTags.URL_GET_BUTTON.getDescription());
        urlButton.setUrl(url);

        InlineKeyboardButton mainMenu = new InlineKeyboardButton();
        mainMenu.setText(MenuButtonTags.URL_BACK_TO_MAIN_MENU.getDescription());
        mainMenu.setCallbackData(MenuButtonTags.URL_BACK_TO_MAIN_MENU.getCommand());
        buttons.add(List.of(urlButton, mainMenu));

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(buttons);
        message.setReplyMarkup(markup);
        executeMessage(message);
    }

    protected EditMessageText editMessage(long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(MenuButtonTags.STATUS_HAS_CHANGED.getDescription());
        editMessage.setMessageId((int) messageId);
        return editMessage;
    }
}