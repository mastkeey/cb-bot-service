package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.aop.StructuralLogWithRequestIdFieldAnnotation;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.enums.Type;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.DocumentService;
import ru.mastkey.telegrambot.service.WorkspaceService;
import ru.mastkey.telegrambot.util.KeyboardUtil;
import ru.mastkey.telegrambot.util.Parser;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
@StructuralLogWithRequestIdFieldAnnotation
public class CallbackHandler {

    private final DocumentService documentService;
    private final DocumentHandler documentHandler;
    private final WorkspaceService workspaceService;
    private final Map<Long, InputState> stateMap;
    private final Map<Long, UUID> currentWorkspaceToUpdateName;
    private final Map<Long, UUID> currentWorkspace;

    public BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> handle(CallbackQuery callbackQuery) {
        String action = Parser.getCallbackAction(callbackQuery.data());
        log.info("Handling callback with action: '{}' from user ID: {}", action, callbackQuery.from().id());

        return switch (action) {
            case "left", "right" -> handleArrow(callbackQuery);
            case "send" -> handleSend(callbackQuery);
            case "UPDATE" -> handleUpdate(callbackQuery);
            case "DELETE" -> handleDelete(callbackQuery);
            case "SELECT" -> handleSelect(callbackQuery);
            case "GET" -> handleGet(callbackQuery);
            default -> {
                log.warn("Unknown callback command: '{}' from user ID: {}", action, callbackQuery.from().id());
                yield new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), UNKNOWN_COMMAND);
            }
        };
    }

    private BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> handleArrow(CallbackQuery callbackQuery) {
        log.info("Handling navigation arrow callback for user ID: {}", callbackQuery.from().id());
        KeyboardInfo data = getData(callbackQuery);

        if (Objects.isNull(data)) {
            log.warn("Navigation data not found for user ID: {}", callbackQuery.from().id());
            return new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), INFORMATION_NOT_FOUND);
        }

        log.info("Updating message with navigation data for user ID: {}", callbackQuery.from().id());
        return KeyboardUtil.updateMessage(
            callbackQuery.maybeInaccessibleMessage().chat().id(),
            callbackQuery.maybeInaccessibleMessage().messageId(),
            callbackQuery.data(),
            data.buttonInfoList(),
            data.pageTotal()
        );
    }

    private KeyboardInfo getData(CallbackQuery callbackQuery) {
        log.info("Retrieving data for navigation callback for user ID: {}", callbackQuery.from().id());
        if (Objects.equals(Parser.getArrowType(callbackQuery.data()), Type.WORKSPACE)) {
            return workspaceService.getWorkspaceList(
                callbackQuery.from().id(),
                Parser.getArrowPageNumber(callbackQuery.data()),
                DEFAULT_PAGE_SIZE
            );
        } else {
            return documentService.getFileList(
                callbackQuery.from().id(),
                currentWorkspace.get(callbackQuery.from().id()),
                Parser.getArrowPageNumber(callbackQuery.data()),
                DEFAULT_PAGE_SIZE
            );
        }
    }

    private SendMessage handleSend(CallbackQuery callbackQuery) {
        log.info("Handling file upload request from user ID: {}", callbackQuery.from().id());
        documentHandler.deleteLastMsg(callbackQuery.from().id());

        boolean res = documentService.uploadFiles(callbackQuery.from().id());
        if (res) {
            log.info("File upload successful for user ID: {}", callbackQuery.from().id());
            return new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), FILE_UPLOAD_SUCCESS);
        } else {
            log.error("File upload failed for user ID: {}", callbackQuery.from().id());
            return new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), FILE_UPLOAD_FAILED);
        }
    }

    private SendMessage handleUpdate(CallbackQuery callbackQuery) {
        log.info("Handling workspace update for user ID: {}", callbackQuery.from().id());
        UUID workspaceId = Parser.getButtonUUID(callbackQuery.data());

        stateMap.put(callbackQuery.from().id(), InputState.WAITING_WORKSPACE_UPDATE_NAME);
        currentWorkspaceToUpdateName.put(callbackQuery.from().id(), workspaceId);

        log.info("User ID: {} is now in update workspace state. Workspace ID: {}", callbackQuery.from().id(), workspaceId);
        return new SendMessage(callbackQuery.from().id(), WAITING_WORKSPACE_NAME_TO_UPDATE);
    }

    private SendMessage handleDelete(CallbackQuery callbackQuery) {
        log.info("Handling delete request for user ID: {}", callbackQuery.from().id());

        if (Objects.equals(Parser.getButtonType(callbackQuery.data()), Type.WORKSPACE)) {
            log.info("Deleting workspace for user ID: {}", callbackQuery.from().id());
            HttpStatus status = workspaceService.deleteWorkspace(
                callbackQuery.from().id(),
                Parser.getButtonUUID(callbackQuery.data())
            );
            if (status.is2xxSuccessful()) {
                log.info("Workspace deleted successfully for user ID: {}", callbackQuery.from().id());
                return new SendMessage(callbackQuery.from().id(), WORKSPACE_DELETE_SUCCESS);
            } else {
                log.error("Failed to delete workspace for user ID: {}", callbackQuery.from().id());
                return new SendMessage(callbackQuery.from().id(), WORKSPACE_DELETE_FAILED);
            }
        } else {
            log.info("Deleting file for user ID: {}", callbackQuery.from().id());
            HttpStatus status = documentService.deleteFile(
                callbackQuery.from().id(),
                currentWorkspace.get(callbackQuery.from().id()),
                Parser.getButtonUUID(callbackQuery.data())
            );
            if (status.is2xxSuccessful()) {
                log.info("File deleted successfully for user ID: {}", callbackQuery.from().id());
                return new SendMessage(callbackQuery.from().id(), FILE_DELETE_SUCCESS);
            } else {
                log.error("Failed to delete file for user ID: {}", callbackQuery.from().id());
                return new SendMessage(callbackQuery.from().id(), FILE_DELETE_FAILED);
            }
        }
    }

    private SendMessage handleSelect(CallbackQuery callbackQuery) {
        log.info("Selecting workspace for user ID: {}", callbackQuery.from().id());
        UUID workspaceId = Parser.getButtonUUID(callbackQuery.data());

        currentWorkspace.put(callbackQuery.from().id(), workspaceId);
        log.info("Workspace selected successfully for user ID: {}. Workspace ID: {}", callbackQuery.from().id(), workspaceId);
        return new SendMessage(callbackQuery.from().id(), WORKSPACE_SELECT_SUCCESS);
    }

    @SneakyThrows
    private BaseRequest<? extends BaseRequest<?, ?>, ? extends BaseResponse> handleGet(CallbackQuery callbackQuery) {
        log.info("Retrieving file for user ID: {}", callbackQuery.from().id());
        MockMultipartFile file = documentService.getFile(
            callbackQuery.from().id(),
            currentWorkspace.get(callbackQuery.from().id()),
            Parser.getButtonUUID(callbackQuery.data())
        );

        if (Objects.nonNull(file)) {
            log.info("File retrieved successfully for user ID: {}", callbackQuery.from().id());
            return new SendDocument(
                callbackQuery.maybeInaccessibleMessage().chat().id(),
                file.getInputStream().readAllBytes()
            ).fileName(file.getName());
        } else {
            log.error("Failed to retrieve file for user ID: {}", callbackQuery.from().id());
            return new SendMessage(callbackQuery.maybeInaccessibleMessage().chat().id(), FILE_GET_FAILED);
        }
    }
}