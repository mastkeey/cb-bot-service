package ru.mastkey.telegrambot.util;


public class Constants {
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int DEFAULT_FILE_CACHE_EXPIRE_TIME = 5;
    public static final int DEFAULT_FILE_MAX_SIZE = 19922944;
    public static final int DEFAULT_FILE_MAX_COUNT = 20;
    public static final int MAX_WORKSPACE_NAME_LENGTH = 20;
    public static final int MAX_FILE_NAME_LENGTH = 30;
    public static final String HEADER_REQUEST_ID = "X-Request-ID";
    public static final String MDC_REQUEST_ID = "RequestId";

    public static final String EMPTY_WORKSPACES = "У вас отсутствуют рабочие пространства";
    public static final String EMPTY_FILES= "В рабочем пространстве отсутствуют файлы";
    public static final String DELETE_FILE_COMMAND_DESCRIPTION = "Выбери файл который хочешь удалить";
    public static final String DOWNLOAD_FILE_COMMAND_DESCRIPTION = "Выбери файл который хочешь получить обратно";
    public static final String CHANGE_WORKSPACE_COMMAND_DESCRIPTION = "Выберите рабочее пространство с которым хотите работать";
    public static final String CONNECT_WORKSPACE_COMMAND_DESCRIPTION = "Подключись к чужому рабочему пространству";
    public static final String CREATE_WORKSPACE_DESCRIPTION = "Создай свое рабочее пространство, чтобы сохранить там сообщение";
    public static final String DELETE_WORKSPACE_COMMAND_DESCRIPTION = "Выбери рабочее пространство, которое хочешь удалить";
    public static final String SHARE_WORKSPACE_COMMAND_DESCRIPTION = "Получи данные для того, чтобы поделиться своими рабочими пространствами";
    public static final String UPDATE_WORKSPACE_COMMAND_DESCRIPTION = "Обнови название своего воркспейса";
    public static final String START_COMMAND_DESCRIPTION = "Инициализация бота";

    public static final String DELETE_FILE_COMMAND = "/delete_file";
    public static final String DOWNLOAD_FILE_COMMAND = "/download";
    public static final String CHANGE_WORKSPACE_COMMAND = "/change";
    public static final String CONNECT_WORKSPACE_COMMAND = "/connect";
    public static final String CREATE_WORKSPACE_COMMAND = "/create";
    public static final String DELETE_WORKSPACE_COMMAND = "/delete_workspace";
    public static final String SHARE_WORKSPACE_COMMAND = "/share";
    public static final String UPDATE_WORKSPACE_COMMAND = "/update";
    public static final String START_COMMAND = "/start";

    public static final String INFORMATION_NOT_FOUND = "Не удалось получить информацию";
    public static final String FILE_LIST_NOT_FOUND = "Не удалось получить список файлов";

    public static final String CHOOSE_FILE_TO_DELETE = "Выбери файл который хочешь удалить";
    public static final String CHOOSE_FILE_TO_GET = "Выбери файл который хочешь получить";
    public static final String CHOOSE_WORKSPACE_TO_SELECT = "Выберите рабочее пространство, которое хотите сделать активным";
    public static final String CHOOSE_WORKSPACE_TO_DELETE = "Выбери рабочее пространство, которе хочешь удалить";
    public static final String CHOOSE_WORKSPACE_TO_UPDATE = "Выбери рабочее пространство, у которого хочешь изменить название";

    public static final String WAITING_WORKSPACE_UUID_INFORMATION = "Введите UUID рабочего пространства которым с вами поделились";
    public static final String WAITING_WORKSPACE_NAME_TO_CREATE = "Введите название рабочего пространства";
    public static final String WAITING_WORKSPACE_NAME_TO_UPDATE = "Введите новое название";
    public static final String WORKSPACE_NAME_TOO_LONG = "Длина имени рабочего пространства не должна превышать 40 символов";
    public static final String FILE_NAME_TOO_LONG = "Длина имени файла с расширением не должна превышать 117 символов";

    public static final String INCORRECT_INPUT = "Некорректный ввод, попробуйте снова";

    public static final String CONNECTION_SUCCESS = "Вы успешно подключились к чужому рабочему пространству";
    public static final String CONNECTION_FAILED = "Не удалось подключиться к чужому рабочему пространству";

    public static final String CREATE_SUCCESS = "Рабочее пространство %s успешно создано";
    public static final String CREATE_FAILED = "Не удалось создать рабочее пространство с названием %s";

    public static final String SHARE_SUCCESS = "Скопируйте UUID и отправьте другому пользователю, чтобы поделиться с ним:\n %s";

    public static final String UPDATE_SUCCESS = "Название успешно изменено";
    public static final String UPDATE_FAILED = "Не удалось изменить название";

    public static final String START_FAILED = "Не удалось зарегистрироваться";

    public static final String FILE_UPLOAD_SUCCESS = "Файлы успешно загружены";
    public static final String FILE_UPLOAD_FAILED = "Не удалось загрузить файлы, попробуйте еще раз";

    public static final String WORKSPACE_DELETE_SUCCESS = "Рабочее пространство успешно удалено";
    public static final String WORKSPACE_DELETE_FAILED = "Не удалось удалить рабочее пространство";

    public static final String FILE_DELETE_SUCCESS = "Файл успешно удален";
    public static final String FILE_DELETE_FAILED = "Не удалось удалить файл";

    public static final String WORKSPACE_SELECT_SUCCESS = "Рабочее пространство успешно выбрано";

    public static final String FILE_GET_FAILED = "Не удалось получить файл";

    public static final String UNKNOWN_COMMAND = "Неизвестная команда";

    public static final String LOAD_DOCUMENT_FAILED = "Не удалось прикрепить файл";
    public static final String LOAD_VOICE_MESSAGE_FAILED = "Не удалось прикрепить голосовое сообщение";
    public static final String LOAD_VIDEO_MESSAGE_FAILED = "Не удалось прикрепить видео сообщение";

    public static final String INVALID_PHOTO_OR_VIDEO = "Если хотите отправить фотографию или видео, загрузите его как файл";
    public static final String INVALID_TYPE = "Данный тип сообщения не поддерживается";

    public static final String FILE_TOO_LARGE = "Файл слишком большой";
    public static final String FILE_UPLOAD_TEMPLATE = "Вы прикрепили %d %s";
    public static final String FILE_SINGULAR = "файл\n";
    public static final String FILE_PLURAL_FEW = "файла\n";
    public static final String FILE_PLURAL_MANY = "файлов\n";
    public static final String MAX_FILE_COUNT_MESSAGE = "Нельзя прикрепить больше 20 файлов\n";

    public static final String HELLO_MESSAGE = """
        Привет, я телеграм бот, который поможет тебе сохранить свои файлы.
        Для того, чтобы начать мною пользоваться, нужно создать свое первое рабочее пространство через команду /create и после выбрать его через команду /change, 
        или подключиться к рабочему пространству другого пользователя через /connect.
        """;
}
