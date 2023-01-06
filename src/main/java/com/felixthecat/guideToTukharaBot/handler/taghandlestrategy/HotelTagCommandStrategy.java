package com.felixthecat.guideToTukharaBot.handler.taghandlestrategy;

import com.felixthecat.guideToTukharaBot.handler.ButtonsOfMenu;
import com.felixthecat.guideToTukharaBot.model.MenuButtonTags;
import com.felixthecat.guideToTukharaBot.model.Tags;
import com.felixthecat.guideToTukharaBot.model.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import one.util.streamex.StreamEx;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.felixthecat.guideToTukharaBot.model.Tags.HOTELS_ITEM;

@Slf4j
@Component
public class HotelTagCommandStrategy extends AbstractTagCommandStrategy {

    private final ButtonsOfMenu buttons;

    public HotelTagCommandStrategy(UserRepository repository, ButtonsOfMenu buttons) {
        super(repository);
        this.buttons = buttons;
    }

    @Override
    public Tags getKey() {
        return HOTELS_ITEM;
    }

    @Override
    public List<BotApiMethod> handle(Update update) {
        val message = update.getMessage();
        val chatId = update.getCallbackQuery().getFrom().getId();
        return List.of(getSendMessage(chatId, buttons.hotelsSectionMenu()));
    }
}
