package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.config.BotConfig;
import com.telegramBots.GuideToBukharaBot.model.ArticleDataRepository;
import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot implements LongPollingBot {

    private final ArticleDataRepository articleDataRepository;

    private final ButtonsOfMenu buttons;

    private final BotConfig config;

    MenuButtonTags menuButtonTags;
    Tags tags;

    public TelegramBot(BotConfig config, ArticleDataRepository articleDataRepository, @Lazy ButtonsOfMenu buttons) {
        this.config = config;
        this.buttons = buttons;
        this.articleDataRepository = articleDataRepository;
    }

    @PostConstruct
    private void init() {
        buttons.drawingTitleMenu();
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

            if (Arrays.stream(MenuButtonTags.values())
                    .map(MenuButtonTags::getCommand)
                    .filter(Objects::nonNull).anyMatch(x -> x.equals(messageText))) {

                menuButtonTags = Arrays.stream(MenuButtonTags.values())
                        .filter(x -> x.getCommand() != null)
                        .filter(x -> x.getCommand().equals(messageText))
                        .findFirst()
                        .get();

                switch (menuButtonTags) {
                    case START:
                        buttons.startCommand(update);
                        break;
                    case USER_DATA:
                        buttons.userDataCommand(chatId);
                        break;
                    case HELP:
                        buttons.helpCommand(chatId);
                        break;
                    case ABOUT_BOT:
                        buttons.aboutBotCommand(chatId);
                        break;
                    case SETTINGS:
                        buttons.changeUserStatus(update);
                        break;
                    default:
                        if (messageText.contains("/addArticle") && chatId == config.getOwner()) {
                            buttons.addDataToArticleRepository(chatId, update.getMessage().getText());
                        }
                }
            } else {
                buttons.wrongRequestFromUser(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            messageText = update.getCallbackQuery().getData();
            chatId = update.getCallbackQuery().getFrom().getId();
            if (Arrays.stream(Tags.values()).map(Tags::toString).anyMatch(x -> x.equalsIgnoreCase(messageText))) {
                tags = Tags.valueOf(messageText);
                switch (tags) {
                    case TOURIST_CHOICE:
                    case LOCAL_CHOICE:
                        buttons.registerUserStatus(tags.getDescription(), chatId);
                        break;
                    case ATTRACTIONS_ITEM:
                        buttons.attractionSectionMenu(chatId);
                        break;
                    case FOOD_ITEM:
                        buttons.foodSectionMenu(chatId);
                        break;
                    case HOTELS_ITEM:
                        buttons.hotelsSectionMenu(chatId);
                        break;
                    default:
                        buttons.sendMessage(chatId, articleDataRepository.getArticleDataById(tags.getDescription()).getData());
                }
            }
        }
    }
}
