package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy.newhandlerstrategy;

import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class DefaultCommandStrategy extends AbstractCommandStrategy {

    public DefaultCommandStrategy(StartCommandsRepository startCommandsRepository) {
        super(startCommandsRepository);
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Неправильная комманда, повторите ввод или воспользуйтесь меню в диалоговом окне!");
        return List.of(message);
    }
}
