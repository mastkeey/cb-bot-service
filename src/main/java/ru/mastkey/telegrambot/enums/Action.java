package ru.mastkey.telegrambot.enums;

public enum Action {
    DELETE("DELETE"), UPDATE("UPDATE"), GET("GET"), SELECT("SELECT"), CONNECT("CONNECT");

    String action;

    Action(String action) {
        this.action = action;
    }


    @Override
    public String toString() {
        return action;
    }
}
