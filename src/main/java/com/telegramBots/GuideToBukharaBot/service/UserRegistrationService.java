package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.Timestamp;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationService {

    private User newUser;

    private final UserRepository userRepository;

    protected void register(Update update){
        newUser = new User();
        var chatId = update.getMessage().getChatId();
        var chat = update.getMessage().getChat();
        newUser.setChatId(chatId);
        newUser.setFirstName(chat.getFirstName());
        newUser.setLastName(chat.getLastName());
        newUser.setUserName(chat.getUserName());
        newUser.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(newUser);
        log.info("User saved: " + newUser);
    }
}
