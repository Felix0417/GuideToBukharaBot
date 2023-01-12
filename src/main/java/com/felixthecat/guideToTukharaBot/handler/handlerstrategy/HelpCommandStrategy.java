package com.felixthecat.guideToTukharaBot.handler.handlerstrategy;

import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class HelpCommandStrategy extends AbstractCommandStrategy {

    public HelpCommandStrategy(StartCommandsRepository startCommandsRepository) {
        super(startCommandsRepository);
    }

    @Override
    public String getKey() {
        return "/help";
    }

    @Override
    public List<BotApiMethod> handler(Update update) {
        return getNewMessage(update.getMessage().getChatId(), getKey(), null);
    }
}
