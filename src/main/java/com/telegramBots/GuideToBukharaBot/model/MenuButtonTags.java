package com.telegramBots.GuideToBukharaBot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MenuButtonTags {
    GREETINGS_TEXT(null, "Привет, %s, рад тебя видеть! "),
    WRONG_REQUEST_FROM_USER(null, "Извините, команда не распознана," +
            " как насчет воспользоваться кнопкой меню в поле ввода?"),

    //title menu
    START("/start", "Перезапустить бота"),
    USER_DATA("/my_data", "Ваши данные"),
    HELP("/help", "О коммандах"),
    ABOUT_BOT("/about", "О боте"),
    SETTINGS("/settings", "Настройки пользователя"),

    //Change user status
    STATUS_TEXT(null, "Выберите ваш статус"),
    STATUS_HAS_CHANGED(null, "Ваш статус успешно изменен!"),
    STATUS_TOURIST(Tags.TOURIST_CHOICE.toString(), "Турист"),
    STATUS_LOCAL(Tags.LOCAL_CHOICE.toString(), "Местный / Релокант"),

    //Main menu
    MAIN_MENU_TEXT(null, "Какие данные вам интересны?"),
    ATTRACTIONS(Tags.ATTRACTIONS_ITEM.toString(), "Достопримечательности"),
    FOOD(Tags.FOOD_ITEM.toString(),"Где поесть"),
    HOTELS(Tags.HOTELS_ITEM.toString(), "Гостиницы"),
    FEATURES_OF_LIFE_EXTENDED_ACCESS("Кнопка для особенностей жизни тут", "Особенности жизни и быта"),
    FLATS_FOR_RENT_EXTENDED_ACCESS("Кнопка для агенств недвижимости", "Аренда жилья долгосрочная"),
    FOOD_PRICES_EXTENDED_ACCESS("Кнопка цены на продукты", "Цены на базовые продукты"),


    //Attractions menu
    ATTRACTIONS_MENU_TEXT(null, "Выберите локацию:"),
    INSIDE_CITY(Tags.ATTRACTIONS_INSIDE_CITY.toString(), "В Городе"),
    OUTSIDE_CITY(Tags.ATTRACTIONS_OUTSIDE_CITY.toString(), "За Городом"),
    GUIDES(Tags.CONTACT_GUIDES.toString(), "Гиды"),

    //Food section menu
    FOOD_SECTION_TEXT(null, "Выберите интересующую вас кухню:"),
    NATIONAL_KITCHEN(Tags.NATIONAL_KITCHEN.toString(), "Национальная кухня"),
    EUROPEAN_KITCHEN(Tags.EUROPEAN_KITCHEN.toString(), "Европейская кухня"),
    ASIAN_KITCHEN(Tags.ASIAN_KITCHEN.toString(), "Азиатская кухня"),

    //Hotels section menu
    HOTELS_MENU_TEXT(null, "Выберите тип отеля:"),
    HOTEL( Tags.HOTELS.toString(), "Гостиница / Отель"),
    GUEST_HOUSE(Tags.GUEST_HOUSES.toString(), "Гостевые дома"),

    // Url button
    URL_TEXT(null, "Вы можете узнать больше информации об этом, или перейти в главное меню"),
    URL_GET_BUTTON(null, "Узнать подробнее"),
    URL_BACK_TO_MAIN_MENU(START.command, "Вернуться в главное меню");

    private final String command;
    private final String description;
}
