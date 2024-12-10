package ru.mastkey.telegrambot.model;

import java.util.UUID;

public record ButtonInfo(
    String name,
    UUID id
) {
}
