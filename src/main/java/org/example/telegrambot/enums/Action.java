package org.example.telegrambot.enums;

public enum Action {
    DELETE("DELETE"), UPDATE("UPDATE"), GET("GET"), SELECT("SELECT");

    String action;

    Action(String action) {
        this.action = action;
    }


    @Override
    public String toString() {
        return action;
    }
}
