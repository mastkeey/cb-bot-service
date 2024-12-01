package org.example.telegrambot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.telegrambot.enums.InputState;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.DocumentService;
import org.example.telegrambot.service.UserService;
import org.example.telegrambot.service.WorkspaceService;
import org.example.telegrambot.util.KeyboardUtil;
import org.example.telegrambot.util.Parser;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CallbackHandler {

    private static final Integer PAGE_SIZE = 3;

    private final DocumentService documentService;
    private final DocumentHandler documentHandler;
    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final Map<Long, InputState> stateMap;
    private final Map<Long, UUID> currentWorkspace;

    public BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> handle(CallbackQuery callbackQuery) {
        if (callbackQuery.data().contains("left") || callbackQuery.data().contains("right")) {
            EditMessageReplyMarkup result = handleArrow(callbackQuery);
            return Objects.isNull(result) ?
                new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), "Не удалось обработать запрос") :
                result;
        } else if (callbackQuery.data().contains("send")) {
            return handleSend(callbackQuery);
        } else if (callbackQuery.data().contains("UPDATE")) {
            return handleUpdate(callbackQuery);
        } else if (callbackQuery.data().contains("DELETE")) {
            return handleDelete(callbackQuery);
        } else if (callbackQuery.data().contains("SELECT")) {
            return handleSelect(callbackQuery);
        } else {
            return handleGet(callbackQuery);
        }
    }

    private EditMessageReplyMarkup handleArrow(CallbackQuery callbackQuery) {
        KeyboardInfo data = getData(callbackQuery);
        return KeyboardUtil.updateMessage(
            callbackQuery.maybeInaccessibleMessage().chat().id(),
            callbackQuery.maybeInaccessibleMessage().messageId(),
            callbackQuery.data(),
            data.buttonInfoList(),
            data.pageTotal()
        );
    }

    private KeyboardInfo getData(CallbackQuery callbackQuery) {
        if (Objects.equals(Parser.getArrowType(callbackQuery.data()), Type.WORKSPACE)) {
            return workspaceService.getWorkspaceList(
                Parser.getArrowPageNumber(callbackQuery.data()), PAGE_SIZE, callbackQuery.from().id()
            );
        } else {
            return documentService.getFileList(
                Parser.getArrowPageNumber(callbackQuery.data()), PAGE_SIZE, callbackQuery.from().id()
            );
        }
    }

    private SendMessage handleSend(CallbackQuery callbackQuery) {
        documentHandler.deleteLastMsg(callbackQuery.from().id());
        boolean res = documentService.uploadFiles(callbackQuery.from().id());
        return res ?
            new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), "Файлы успешно загружены") :
            new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), "Не удалось загрузить файлы, попробуйте еще раз");
    }

    private SendMessage handleUpdate(CallbackQuery callbackQuery) {
        UUID workspaceId = Parser.getButtonUUID(callbackQuery.data());
        stateMap.put(callbackQuery.from().id(), InputState.WAITING_WORKSPACE_UPDATE_NAME);
        currentWorkspace.put(callbackQuery.from().id(), workspaceId);
        return new SendMessage(callbackQuery.from().id(), "Введите новое название");
    }

    private SendMessage handleDelete(CallbackQuery callbackQuery) {
        if (Objects.equals(Parser.getButtonType(callbackQuery.data()), Type.WORKSPACE)) {
            HttpStatus httpStatus = workspaceService.deleteWorkspace(Parser.getButtonUUID(callbackQuery.data()));
            return httpStatus.is2xxSuccessful() ?
                new SendMessage(callbackQuery.from().id(), "Рабочее пространство успешно удалено") :
                new SendMessage(callbackQuery.from().id(), "Не удалось удалить рабочее пространство");
        } else {
            HttpStatus httpStatus = documentService.deleteFile(Parser.getButtonUUID(callbackQuery.data()));
            return httpStatus.is2xxSuccessful() ?
                new SendMessage(callbackQuery.from().id(), "Файл успешно удален") :
                new SendMessage(callbackQuery.from().id(), "Не удалось удалить файл");
        }
    }

    private SendMessage handleSelect(CallbackQuery callbackQuery) {
        HttpStatus httpStatus = userService.selectWorkspace(callbackQuery.from().id(), Parser.getButtonUUID(callbackQuery.data()));
        return httpStatus.is2xxSuccessful() ?
            new SendMessage(callbackQuery.from().id(), "Рабочее пространство успешно выбрано") :
            new SendMessage(callbackQuery.from().id(), "Не удалось выбрать рабочее пространство");
    }

    @SneakyThrows
    private BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> handleGet(CallbackQuery callbackQuery) {
        MockMultipartFile file = documentService.getFile(Parser.getButtonUUID(callbackQuery.data()));
        return Objects.nonNull(file) ?
            new SendDocument(
                callbackQuery.maybeInaccessibleMessage().chat().id(),
                file.getInputStream().readAllBytes()
            ).fileName(file.getName()) :
            new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), "Не удалось получить файл");
    }
}
