package com.felixthecat.guideToTukharaBot.bot;

import com.felixthecat.guideToTukharaBot.entity.StartCommands;
import com.felixthecat.guideToTukharaBot.model.StartCommandsRepository;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;
import java.util.stream.Collectors;


@Configuration
@RequiredArgsConstructor
public class LeftMenuButtonsBeanConfiguration {

    private final StartCommandsRepository startCommandsRepository;

    @Bean
    public List<BotCommand> leftMenu() {
        return StreamEx.of(startCommandsRepository.findAll().spliterator())
                .map(this::create)
                .collect(Collectors.toList());
    }

    private BotCommand create(StartCommands command) {
        return new BotCommand(command.getCommand(), command.getDescription());
    }
}
