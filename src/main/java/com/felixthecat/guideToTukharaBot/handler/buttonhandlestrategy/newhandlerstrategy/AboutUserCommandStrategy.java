package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy.newhandlerstrategy;

import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AboutUserCommandStrategy extends AbstractCommandStrategy {

    private final StartCommandsRepository startCommandsRepository;
    private final UserRepository userRepository;

    public AboutUserCommandStrategy(StartCommandsRepository startCommandsRepository, UserRepository userRepository) {
        super(startCommandsRepository);
        this.startCommandsRepository = startCommandsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String getKey() {
        return "/my_data";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        var message = new SendMessage();
        var chatId = update.getMessage().getChat().getId();
        message.setChatId(String.valueOf(chatId));
        var text = startCommandsRepository.getByCommand(getKey()).getStartMessageText();
        var user = userRepository.findById(chatId).get();
        var localDateTime = LocalDateTime
                .ofInstant(user.getRegisteredAt(), ZoneId.of("Asia/Samarkand"))
                .format(DateTimeFormatter.ofPattern("d.MM.yyyy HH:mm"));
        message.setText(String.format(text, user.getChatId(), user.getUserName(), localDateTime));
        return List.of(message);
    }
}