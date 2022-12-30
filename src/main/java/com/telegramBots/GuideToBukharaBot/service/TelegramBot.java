package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.config.BotConfig;
import com.telegramBots.GuideToBukharaBot.model.ArticleDataRepository;
import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot implements LongPollingBot {
    private final ArticleDataRepository articleDataRepository;
    private final ButtonsOfMenu buttons;
    private final BotConfig config;
    private MenuButtonTags menuButtonTags;
    private Tags tags;

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    private void executeMessage(SendMessage message) {
        execute(message);
    }

    protected void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        executeMessage(message);
    }

    void wrongRequestFromUser(long chatId){
        sendMessage(chatId, MenuButtonTags.WRONG_REQUEST_FROM_USER.getDescription());
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        long chatId;
        String messageText;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();

            if (Arrays.stream(MenuButtonTags.values()).map(MenuButtonTags::getCommand).filter(Objects::nonNull).anyMatch(x -> x.equals(messageText))) {

                menuButtonTags = Arrays.stream(MenuButtonTags.values()).filter(x -> x.getCommand() != null).filter(x -> x.getCommand().equals(messageText)).findFirst().get();

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
                        execute(buttons.editMessage(chatId, update.getCallbackQuery().getMessage().getMessageId()));
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
                        sendMessage(chatId, articleDataRepository.getArticleDataById(tags.getDescription()).getData());
                        String url = articleDataRepository.getArticleDataByAddress(tags.getDescription()).getAddress();
                        if (url != null) {
                            buttons.drawingUrlButton(chatId, url);
                        }
                }
            } else if (MenuButtonTags.URL_BACK_TO_MAIN_MENU.getCommand().equals(messageText)) {
                buttons.startMainMenu(chatId);
            }
        }
    }
}
