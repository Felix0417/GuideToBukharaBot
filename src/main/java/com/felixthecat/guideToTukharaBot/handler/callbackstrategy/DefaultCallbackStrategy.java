package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import com.felixthecat.guideToTukharaBot.model.ArticleDataRepository;
import com.felixthecat.guideToTukharaBot.model.CallbacksRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class DefaultCallbackStrategy extends AbstractCallbackStrategy {

    public DefaultCallbackStrategy(ArticleDataRepository articleDataRepository, CallbacksRepository callbacksRepository, UserRepository userRepository) {
        super(articleDataRepository, callbacksRepository, userRepository);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var sendMessage = new SendMessage();
        var message = update.getCallbackQuery().getMessage();
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setText("Неправильная комманда, повторите ввод или воспользуйтесь меню в диалоговом окне!");
        return List.of(sendMessage);
    }
}
