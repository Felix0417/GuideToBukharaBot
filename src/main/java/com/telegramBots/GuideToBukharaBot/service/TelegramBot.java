package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.config.BotConfig;
import com.telegramBots.GuideToBukharaBot.model.ArticleDataRepository;
import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.model.Tags;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot implements LongPollingBot {
    private final ArticleDataRepository articleDataRepository;
    private final ButtonsOfMenu buttons;
    private final BotConfig config;
    private final UserRegistrationService userRegistrationService;
    private final LoadLeftMenu leftMenu;
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

    void wrongRequestFromUser(long chatId) {
        sendMessage(chatId, MenuButtonTags.WRONG_REQUEST_FROM_USER.getDescription());
    }

    @SneakyThrows
    @PostConstruct
    private void initLeftMenu() {
        execute(new SetMyCommands(leftMenu.getListOfCommands(), new BotCommandScopeDefault(), "ru"));
    }

    protected void drawingButtons(long chatId, List<MenuButtonTags> tags) {
        var buttons = StreamEx.of(tags)
                .skip(1L)
                .map(this::getButtonList)
                .toList();

        var message = messageForDrawingButtons(chatId, tags.get(0));
        message.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));

        executeMessage(message);
    }

    protected void drawingUrlButton(long chatId, String url) {
        var buttons = new ArrayList<List<InlineKeyboardButton>>();
        buttons.add(List.of(
                getInlineKeyboardButton(MenuButtonTags.URL_GET_BUTTON, url),
                getInlineKeyboardButton(MenuButtonTags.URL_BACK_TO_MAIN_MENU, null)));

        var message = messageForDrawingButtons(chatId, MenuButtonTags.URL_TEXT);
        message.setReplyMarkup(getNewInLineKeyboardMarkup(buttons));

        executeMessage(message);
    }

    protected SendMessage messageForDrawingButtons(long chatId, MenuButtonTags tagFirst) {
        var message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(tagFirst.getDescription());
        return message;
    }

    protected InlineKeyboardMarkup getNewInLineKeyboardMarkup(List<List<InlineKeyboardButton>> buttons) {
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    protected InlineKeyboardButton getInlineKeyboardButton(MenuButtonTags tag, String text) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(tag.getDescription());
        if (null != text) {
            inlineKeyboardButton.setUrl(text);
        } else {
            inlineKeyboardButton.setCallbackData(tag.getCommand());
        }
        return inlineKeyboardButton;
    }

    private List<InlineKeyboardButton> getButtonList(MenuButtonTags tag) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setCallbackData(tag.getCommand());
        inlineKeyboardButton.setText(tag.getDescription());
        return List.of(inlineKeyboardButton);
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        long chatId;
        String messageText;

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();

            if (Arrays.stream(MenuButtonTags.values())
                    .map(MenuButtonTags::getCommand)
                    .filter(Objects::nonNull)
                    .anyMatch(x -> x.equals(messageText))) {

                menuButtonTags = Arrays.stream(MenuButtonTags.values()).filter(x -> x.getCommand() != null)
                        .filter(x -> x.getCommand().equals(messageText))
                        .findFirst()
                        .get();

                switch (menuButtonTags) {
                    case START:
                        sendMessage(chatId, buttons.startCommand(update));
                        if (!buttons.containsUserInRepository(chatId)) {
                            userRegistrationService.register(update);
                            drawingButtons(chatId, buttons.changeUserStatus());
                            break;
                        }
                        drawingButtons(chatId, buttons.startMainMenu(chatId));
                        break;
                    case USER_DATA:
                        sendMessage(chatId, buttons.userDataCommand(chatId));
                        break;
                    case HELP:
                        sendMessage(chatId, buttons.helpCommand());
                        break;
                    case ABOUT_BOT:
                        sendMessage(chatId, buttons.aboutBotCommand());
                        break;
                    case SETTINGS:
                        drawingButtons(chatId, buttons.changeUserStatus());
                        break;
                }
            } else {
                wrongRequestFromUser(chatId);
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
                        drawingButtons(chatId, buttons.attractionSectionMenu());
                        break;
                    case FOOD_ITEM:
                        drawingButtons(chatId, buttons.foodSectionMenu());
                        break;
                    case HOTELS_ITEM:
                        drawingButtons(chatId, buttons.hotelsSectionMenu());
                        break;
                    default:
                        sendMessage(chatId, articleDataRepository.getArticleDataById(tags.getDescription()).getData());
                        String url = articleDataRepository.getArticleDataByAddress(tags.getDescription()).getAddress();
                        if (url != null) {
                            drawingUrlButton(chatId, url);
                        }
                }
            } else if (MenuButtonTags.URL_BACK_TO_MAIN_MENU.getCommand().equals(messageText)) {
                buttons.startMainMenu(chatId);
            }
        }
    }
}
