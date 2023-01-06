package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.Tags.TOURIST_CHOICE;

@Slf4j
@Component
public class TouristTagCommandStrategy extends AbstractTagCommandStrategy {

    public TouristTagCommandStrategy(UserRepository repository) {
        super(repository);
    }

    @Override
    public Tags getKey() {
        return TOURIST_CHOICE;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        return getUpdatedBotApiMethodList(update, TOURIST_CHOICE);
    }

}