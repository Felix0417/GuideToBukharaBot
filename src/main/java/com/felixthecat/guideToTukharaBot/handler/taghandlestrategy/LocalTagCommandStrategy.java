package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.Tags.LOCAL_CHOICE;

@Slf4j
@Component
public class LocalTagCommandStrategy extends AbstractTagCommandStrategy {

    public LocalTagCommandStrategy(UserRepository repository) {
        super(repository);
    }

    @Override
    public Tags getKey() {
        return LOCAL_CHOICE;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        return getUpdatedBotApiMethodList(update, LOCAL_CHOICE);
    }
}
