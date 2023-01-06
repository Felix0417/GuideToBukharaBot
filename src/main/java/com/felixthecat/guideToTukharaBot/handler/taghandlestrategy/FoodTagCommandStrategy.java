package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.felixthecat.guideToTukharaBot.handler.ButtonsOfMenu;
import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.Tags.FOOD_ITEM;

@Slf4j
@Component
public class FoodTagCommandStrategy extends AbstractTagCommandStrategy{

    private final ButtonsOfMenu buttons;

    public FoodTagCommandStrategy(UserRepository repository, ButtonsOfMenu buttons) {
        super(repository);
        this.buttons = buttons;
    }

    @Override
    public Tags getKey() {
        return FOOD_ITEM;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = update.getCallbackQuery().getFrom().getId();
        return List.of(getSendMessage(chatId, buttons.foodSectionMenu()));
    }
}
