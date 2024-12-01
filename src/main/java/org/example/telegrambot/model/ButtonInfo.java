package org.example.telegrambot.model;

import java.util.UUID;

public record ButtonInfo(
    String name,
    UUID id
) {
}
