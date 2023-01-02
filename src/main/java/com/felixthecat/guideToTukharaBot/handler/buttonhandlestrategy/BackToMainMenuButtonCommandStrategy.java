package com.felixthecat.guideToTukharaBot.handler.buttonhandlestrategy;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import com.telegramBots.GuideToBukharaBot.service.ButtonsOfMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.telegramBots.GuideToBukharaBot.model.MenuButtonTags.URL_BACK_TO_MAIN_MENU;

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
