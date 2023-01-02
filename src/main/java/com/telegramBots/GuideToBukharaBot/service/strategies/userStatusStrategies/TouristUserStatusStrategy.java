package com.telegramBots.GuideToBukharaBot.service.strategies.userStatusStrategies;

import com.telegramBots.GuideToBukharaBot.entity.User;
import com.telegramBots.GuideToBukharaBot.model.UserRepository;
import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;


import static com.telegramBots.GuideToBukharaBot.model.Tags.LOCAL_CHOICE;
import static com.telegramBots.GuideToBukharaBot.model.Tags.TOURIST_CHOICE;

@Component
@Slf4j
public class TouristUserStatusStrategy extends AbstractUserStatusStrategy{

    private final UserRepository repository;

    public TouristUserStatusStrategy(ButtonsOfMenu buttons, UserRepository repository) {
        super(TOURIST_CHOICE);
        this.repository = repository;
    }

    @Override
    public EditMessageText apply(long chatId, long messageId ) {
        User user = repository.findById(chatId).get();
        user.setStatus(TOURIST_CHOICE.getDescription());
        repository.save(user);
        log.info("User has update settings: " + user);

        return editMessage(chatId, messageId);
    }
}
