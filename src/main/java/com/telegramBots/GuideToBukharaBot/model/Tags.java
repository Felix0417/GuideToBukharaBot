package com.telegramBots.GuideToBukharaBot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Tags {
    HELP("Помощь"),
    USER_DATA("О_пользователе"),
    ABOUT_BOT("О_боте"),
    TOURIST_CHOICE("Турист"),
    LOCAL_CHOICE("Местный"),
    ATTRACTIONS_ITEM("Достопримечательности"),
    FOOD_ITEM("Еда"),
    HOTELS_ITEM("Отели"),
    ATTRACTIONS_INSIDE_CITY("Достопримечательности_город"),
    ATTRACTIONS_OUTSIDE_CITY("Достопримечательности_регион"),
    CONTACT_GUIDES("Гиды"),
    NATIONAL_KITCHEN("Рестораны_национальной_кухни"),
    EUROPEAN_KITCHEN("Рестораны_европейской_кухни"),
    ASIAN_KITCHEN("Рестораны_азиатской_кухни"),
    HOTELS("Отели"),
    GUEST_HOUSES("Гостевые_дома");

    private final String description;

}
