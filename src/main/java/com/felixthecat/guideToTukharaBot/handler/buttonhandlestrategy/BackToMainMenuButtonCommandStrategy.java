package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.felixthecat.guideToTukharaBot.handler.ButtonsOfMenu;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.MenuButtonTags.URL_BACK_TO_MAIN_MENU;

@Component
@RequiredArgsConstructor
public class BackToMainMenuButtonCommandStrategy implements ButtonCommandStrategy {

    private final ButtonsOfMenu buttons;

    @Override
    public MenuButtonTags getKey() {
        return URL_BACK_TO_MAIN_MENU;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        //buttons.startMainMenu(chatId);
        return List.of();
    }
}
