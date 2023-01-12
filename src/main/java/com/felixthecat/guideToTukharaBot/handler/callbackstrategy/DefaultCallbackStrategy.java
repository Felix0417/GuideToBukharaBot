package com.felixthecat.guideToTukharaBot.handler.callbackstrategy;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class DefaultCallbackStrategy extends AbstractCallbackStrategy{

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
