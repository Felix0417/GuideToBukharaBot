package com.felixthecat.guideToTukharaBot.handler;

import com.felixthecat.guideToTukharaBot.entity.User;
import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ButtonsOfMenu {

    private static final List<MenuButtonTags> TOURIST_CHOICE_LIST = List.of(
            MenuButtonTags.MAIN_MENU_TEXT,
            MenuButtonTags.ATTRACTIONS,
            MenuButtonTags.FOOD,
            MenuButtonTags.HOTELS);

    private static final List<MenuButtonTags> LOCAL_CHOICE_LIST = List.of(
            MenuButtonTags.MAIN_MENU_TEXT,
            MenuButtonTags.FOOD,
            MenuButtonTags.FLATS_FOR_RENT_EXTENDED_ACCESS,
            MenuButtonTags.FEATURES_OF_LIFE_EXTENDED_ACCESS,
            MenuButtonTags.FOOD_PRICES_EXTENDED_ACCESS
    );

    private final UserRepository userRepository;
    private final ArticleDataRepository articleDataRepository;

    public String startCommand(Update update) {
        var chat = update.getMessage().getChat();
        String answer = String.format(MenuButtonTags.GREETINGS_TEXT.getDescription(), chat.getUserName());
        log.info("Replied to user " + chat.getUserName());
        return answer;
    }

    public boolean containsUserInRepository(long chatId) {
        return userRepository.findById(chatId).isPresent();
    }

    public String userDataCommand(long chatId) {
        Optional<User> user = userRepository.findById(chatId);
        String regDate = new SimpleDateFormat("d.MM.yyyy hh:mm").format(user.get().getRegisteredAt());
        return String.format(
                articleDataRepository.getArticleDataById(Tags.USER_DATA.getDescription()).getData(),
                user.get().getChatId(),
                user.get().getUserName(),
                regDate);
    }

    public String helpCommand() {
        return articleDataRepository.getArticleDataById(Tags.HELP.getDescription()).getData();
    }

    public String aboutBotCommand() {
        return articleDataRepository.getArticleDataById(Tags.ABOUT_BOT.getDescription()).getData();
    }

    public List<MenuButtonTags> changeUserStatus() {
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
    }

    public List<MenuButtonTags> startMainMenu(long chatId) {
        if (userRepository.findById(chatId).get().getStatus().equals(Tags.TOURIST_CHOICE.getDescription())) {
            return TOURIST_CHOICE_LIST;
        } else if (userRepository.findById(chatId).get().getStatus().equals(Tags.LOCAL_CHOICE.getDescription())) {
            return LOCAL_CHOICE_LIST;
        } else {
            return List.of();
        }
    }

    public List<MenuButtonTags> attractionSectionMenu() {
        return List.of(
                MenuButtonTags.ATTRACTIONS_MENU_TEXT,
                MenuButtonTags.INSIDE_CITY,
                MenuButtonTags.OUTSIDE_CITY,
                MenuButtonTags.GUIDES);
    }

    public List<MenuButtonTags> foodSectionMenu() {
        return List.of(
                MenuButtonTags.FOOD_SECTION_TEXT,
                MenuButtonTags.NATIONAL_KITCHEN,
                MenuButtonTags.EUROPEAN_KITCHEN,
                MenuButtonTags.ASIAN_KITCHEN);
    }

    public List<MenuButtonTags> hotelsSectionMenu() {
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