package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

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

    protected String startCommand(Update update) {
        var chatId = update.getMessage().getChatId();
        var chat = update.getMessage().getChat();
        String answer = String.format(MenuButtonTags.GREETINGS_TEXT.getDescription(), chat.getUserName());
        log.info("Replied to user " + chat.getUserName());
        return answer;
    }

    protected boolean containsUserInRepository(long chatId){
        return userRepository.findById(chatId).isPresent();
    }

    protected String userDataCommand(long chatId){
        Optional<User> user = userRepository.findById(chatId);
        String regDate = new SimpleDateFormat("d.MM.yyyy hh:mm").format(user.get().getRegisteredAt());
        return String.format(
                articleDataRepository.getArticleDataById(Tags.USER_DATA.getDescription()).getData(),
                user.get().getChatId(),
                user.get().getUserName(),
                regDate);
    }

    protected String helpCommand(){
        return articleDataRepository.getArticleDataById(Tags.HELP.getDescription()).getData();
    }

    protected String aboutBotCommand(){
        return articleDataRepository.getArticleDataById(Tags.ABOUT_BOT.getDescription()).getData();
    }

    protected List<MenuButtonTags> changeUserStatus() {
        return List.of(
                MenuButtonTags.STATUS_TEXT,
                MenuButtonTags.STATUS_TOURIST,
                MenuButtonTags.STATUS_LOCAL);
    }

    protected void registerUserStatus(String data, long chatId) {
        User user = userRepository.findById(chatId).get();
        user.setStatus(data);
        userRepository.save(user);
        log.info("User has update settings: " + user);
        startMainMenu(chatId);
    }

    protected List<MenuButtonTags> startMainMenu(long chatId) {
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
        return mainMenuTags;
    }

    protected List<MenuButtonTags> attractionSectionMenu(){
        return List.of(
                MenuButtonTags.ATTRACTIONS_MENU_TEXT,
                MenuButtonTags.INSIDE_CITY,
                MenuButtonTags.OUTSIDE_CITY,
                MenuButtonTags.GUIDES);
    }

    protected List<MenuButtonTags> foodSectionMenu(){
        return List.of(
                MenuButtonTags.FOOD_SECTION_TEXT,
                MenuButtonTags.NATIONAL_KITCHEN,
                MenuButtonTags.EUROPEAN_KITCHEN,
                MenuButtonTags.ASIAN_KITCHEN);
    }

    protected List<MenuButtonTags> hotelsSectionMenu(){
        return List.of(
                MenuButtonTags.HOTELS_MENU_TEXT,
                MenuButtonTags.HOTEL,
                MenuButtonTags.GUEST_HOUSE);
    }

    protected EditMessageText editMessage(long chatId, long messageId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setText(MenuButtonTags.STATUS_HAS_CHANGED.getDescription());
        editMessage.setMessageId((int) messageId);
        return editMessage;
    }
}