package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;

    protected void register(Update update) {
        var chatId = update.getMessage().getChatId();
        var chat = update.getMessage().getChat();
        val user = User.builder()
                .chatId(chatId)
                .firstName(chat.getFirstName())
                .lastName(chat.getLastName())
                .userName(chat.getUserName())
                .registeredAt(new Timestamp(System.currentTimeMillis()))
                .build();

        userRepository.save(user);
        log.info("User saved: " + user);
    }
}
