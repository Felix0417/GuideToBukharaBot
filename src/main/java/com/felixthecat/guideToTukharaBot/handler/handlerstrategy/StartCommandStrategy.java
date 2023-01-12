package com.felixthecat.guideToTukharaBot.handler.handlerstrategy;

import com.felixthecat.guideToTukharaBot.entity.Callbacks;
import com.felixthecat.guideToTukharaBot.entity.User;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;
import java.util.List;

@Component
public class StartCommandStrategy extends AbstractCommandStrategy {

    private final CallbacksRepository callbacksRepository;
    private final UserRepository userRepository;

    public StartCommandStrategy(StartCommandsRepository startCommandsRepository, CallbacksRepository callbacksRepository, UserRepository userRepository) {
        super(startCommandsRepository);
        this.callbacksRepository = callbacksRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String getKey() {
        return "/start";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var chatId = update.getMessage().getChatId();

        List<Callbacks> callbacksList;
        if (!userRepository.existsById(chatId)) {
            saveUser(update);
            callbacksList = callbacksRepository.getAllByUserStatus("Выбор статуса");
            return getNewMessage(chatId, SettingsCommandStrategy.getStaticKey(), callbacksList);
        }
        var user = userRepository.findById(chatId).get();
        callbacksList = callbacksRepository.getAllByUserStatus(user.getStatus());
        return getNewMessage(chatId, getKey(), callbacksList);
    }

    public void saveUser(Update update) {
        User newUser = new User();
        var chat = update.getMessage().getChat();
        newUser.setChatId(chat.getId());
        newUser.setFirstName(chat.getFirstName());
        newUser.setLastName(chat.getLastName());
        newUser.setUserName(chat.getUserName());
        newUser.setRegisteredAt(Instant.now());
        userRepository.save(newUser);
    }
}
