package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LoadLeftMenu {

    TelegramBot bot;

//    @PostConstruct
    private void loadMenu(){
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(MenuButtonTags.START.getCommand(), MenuButtonTags.START.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.USER_DATA.getCommand(), MenuButtonTags.USER_DATA.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.HELP.getCommand(), MenuButtonTags.HELP.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.ABOUT_BOT.getCommand(), MenuButtonTags.ABOUT_BOT.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.SETTINGS.getCommand(), MenuButtonTags.SETTINGS.getDescription()));

        try {
            bot.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), "ru"));
        } catch (TelegramApiException e) {
            log.error("Error setting's bot command list: " + e.getMessage());
        }
    }
}
