package com.telegramBots.GuideToBukharaBot.service;

import com.telegramBots.GuideToBukharaBot.model.MenuButtonTags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoadLeftMenu {

    //private final заставит сгенерить аннотацию @ReqArgsCons с этим полем. Спринг автоматом инжектит бины, если конструктор
    //один, поэтому дополнительных вещей типа (onConstructor = @__(@Autowired)) не надо добавлять
    private final TelegramBot bot;
    private final List<BotCommand> listOfCommands = new ArrayList<>();

    @PostConstruct
    private void loadMenu() {

        listOfCommands.add(new BotCommand(MenuButtonTags.START.getCommand(), MenuButtonTags.START.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.USER_DATA.getCommand(), MenuButtonTags.USER_DATA.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.HELP.getCommand(), MenuButtonTags.HELP.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.ABOUT_BOT.getCommand(), MenuButtonTags.ABOUT_BOT.getDescription()));
        listOfCommands.add(new BotCommand(MenuButtonTags.SETTINGS.getCommand(), MenuButtonTags.SETTINGS.getDescription()));

        try {
            bot.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), "ru"));
        } catch (Exception e) {
            log.error("Error setting's bot command list: " + e.getMessage());
        }
    }
}
