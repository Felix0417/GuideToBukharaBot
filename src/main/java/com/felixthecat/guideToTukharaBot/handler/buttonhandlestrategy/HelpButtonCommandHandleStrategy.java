package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.felixthecat.guideToTukharaBot.handler.ButtonsOfMenu;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.MenuButtonTags.HELP;

@Component
@RequiredArgsConstructor
public class HelpButtonCommandHandleStrategy implements ButtonCommandStrategy {
    private final ButtonsOfMenu buttons;

    @Override
    public MenuButtonTags getKey() {
        return HELP;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = message.getChatId();
        var result = new SendMessage();
        result.setText(buttons.helpCommand());
        result.setChatId(String.valueOf(chatId));
        return List.of(result);
    }
}
