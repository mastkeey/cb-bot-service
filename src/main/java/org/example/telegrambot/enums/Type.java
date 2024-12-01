package org.example.telegrambot.enums;

public enum Type {
    WORKSPACE("WORKSPACE"), FILE("FILE");

    final String type;

    Type(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return type;
    }


}
