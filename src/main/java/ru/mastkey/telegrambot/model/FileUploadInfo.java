package ru.mastkey.telegrambot.model;

import com.pengrad.telegrambot.model.File;

import java.util.Objects;

public record FileUploadInfo(
    String name,
    File file
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileUploadInfo that = (FileUploadInfo) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
