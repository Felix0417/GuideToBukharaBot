package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import com.felixthecat.guideToTukharaBot.entity.ArticleData;
import com.felixthecat.guideToTukharaBot.entity.Callbacks;
import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageAutoDeleteTimerChanged;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCallbackStrategy implements CallbackStrategy {

    private final ArticleDataRepository articleDataRepository;
    private final CallbacksRepository callbacksRepository;
    private final UserRepository userRepository;

    private static final String GET_URL_BUTTON_TEXT = "Узнать подробнее";
    private static final String BACK_BUTTON_TEXT = "⬅️Назад";
    private static final String MAIN_MENU_TEXT = "Какие данные вам интересны?";


    public List<BotApiMethod> getNewMenuMessage(Message message, String text, List<Callbacks> callbacksList) {
        var newMessage = new SendMessage();
        newMessage.setChatId(String.valueOf(message.getChatId()));
        newMessage.setText(text);
        newMessage.setReplyMarkup(getInlineReplyMarkup(callbacksList));
        return List.of(getDeleteMessage(message), newMessage);
    }

    protected List<Callbacks> getButtonsListForMenu(String menuSection) {
        return callbacksRepository.getAllByButtonType(getKey());
    }

    protected InlineKeyboardMarkup getInlineReplyMarkup(List<Callbacks> buttons) {
        var newButtons = StreamEx.of(buttons)
                .map(this::getInlineKeyboardButton)
                .toList();
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(newButtons);
        return keyboardMarkup;
    }

    protected List<InlineKeyboardButton> getInlineKeyboardButton(Callbacks button) {
        var inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(button.getDescription());
        var command = button.getCommand();
        if (command.matches("(https://).+")) {
            inlineKeyboardButton.setUrl(command);
        } else {
            inlineKeyboardButton.setCallbackData(button.getCommand());
        }
        return List.of(inlineKeyboardButton);
    }

    protected List<BotApiMethod> getEndlessMessage(Update update, String key) {
        var message = update.getCallbackQuery().getMessage();
        var articleData = articleDataRepository.getArticleDataByCommand(getKey());
        var backCallbacks = callbacksRepository.getCallbacksByCommand(getKey());
        return getNewMenuMessage(message, articleData.getData(), getEndlessCallbackList(articleData, backCallbacks));
    }

    protected List<Callbacks> getEndlessCallbackList(ArticleData articleData, Callbacks callbacks) {
        var buttonWithUrl = new Callbacks();
        buttonWithUrl.setDescription(GET_URL_BUTTON_TEXT);
        buttonWithUrl.setCommand(articleData.getAddress());

        callbacks.setDescription(BACK_BUTTON_TEXT);
        return List.of(buttonWithUrl, callbacks);
    }

    protected List<BotApiMethod> saveUserStatusAndGetMainMenu(Update update, String status) {
        var message = update.getCallbackQuery().getMessage();
        saveUserStatus(message);
        return getMainMenu(message);
    }

    protected void saveUserStatus(Message message) {
        var user = userRepository.findById(message.getChatId()).get();
        user.setStatus(this.getKey());
        userRepository.save(user);
        log.info(String.format("User %d has change his status - %s", message.getChatId(), this.getKey()));
    }

    protected List<BotApiMethod> getMainMenu(Message message){
        var userStatus = userRepository.findById(message.getChatId()).get().getStatus();
        var callbacks = callbacksRepository.getAllByUserStatus(userStatus);
        return getNewMenuMessage(message, MAIN_MENU_TEXT, callbacks);
    }

    protected BotApiMethod getDeleteMessage(Message message) {
        var deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(message.getChatId()));
        deleteMessage.setMessageId(message.getMessageId());
        return deleteMessage;
    }
}
