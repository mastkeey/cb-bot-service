package ru.mastkey.telegrambot.model;

import ru.mastkey.model.FileResponse;
import ru.mastkey.model.WorkspaceResponse;

import java.util.List;

public record KeyboardInfo(
    Integer pageTotal,
    List<ButtonInfo> buttonInfoList
) {

    static public ButtonInfo parseInfo(FileResponse fileResponse) {
        return new ButtonInfo(fileResponse.getFileName(), fileResponse.getFileId());
    }

    static public ButtonInfo parseInfo(WorkspaceResponse workspaceResponse) {
        return new ButtonInfo(workspaceResponse.getName(), workspaceResponse.getWorkspaceId());
    }
}
