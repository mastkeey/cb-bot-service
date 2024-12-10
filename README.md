# cb-bot-service

Этот репозиторий содержит микросервис **CB Bot Service**, который реализует основную логику общения с пользователем в приложении. Микросервис разработан с использованием Java и Spring Boot, а для управления сборкой и зависимостями используется Maven.

## Содержание

- [Описание проекта](#описание-проекта)
- [Структура репозитория](#структура-репозитория)
- [Сборка и запуск](#сборка-и-запуск)
    - [Предварительные требования](#предварительные-требования)
    - [Локальный запуск](#локальный-запуск)
    - [Запуск через Docker Compose](#запуск-через-docker-compose-из-server-configuration)
- [CI/CD](#cicd)

---

## Описание проекта

**CB Bot Service** выполняет следующую задачу:
- Взаимодействие с пользователем.

Основные технологии:
- **Java 17**
- **Spring Boot**
- **Docker**
- **Maven**

---

## API контракт сервиса

Для взаимодействия с основным микросервисом бот использует этот контракт:
[API контракт](https://github.com/mastkeey/cb-cloud-service-open-api)

## Структура репозитория

```text
cb-bot-service/
│
├── .github/
│   ├── workflows/
│       ├── buildTestPublish.yml   # CI/CD Workflow для сборки, тестирования и публикации
│       ├── deploy.yml             # Workflow для деплоя
│
├── src/
│   ├── main/                      # Исходный код приложения
│   ├── test/                      # Тесты приложения
│
├── .gitignore                     # Исключения для Git
├── Dockerfile                     # Dockerfile для сборки образа
├── mvnw, mvnw.cmd                 # Maven Wrapper
├── pom.xml                        # Maven файл для сборки
└── README.md                      # Документация
```
## Сборка и запуск

### Предварительные требования

Для корректной работы приложения требуются:
1. **CB Cloud Service**
    - Убедитесь, что сервис запущен и доступен.
    - Укажите следующие переменные окружения:
        - `CB_URL`: URL на Cloud Service.

### Локальный запуск

1. Убедитесь, что PostgreSQL и MinIO запущены и доступны.
2. Задайте переменные окружения:
   ```bash
   export CB_URL=url
   export TOKEN_TTL=10
   export BOT_TOKEN=token
   ```
3. Выполните команду для запуска приложения:
   ```bash
   mvn spring-boot:run
   ```
### Запуск через Docker Compose из [Server configuration](https://github.com/mastkeey/cb-server-config)

1. Настройте файл `.env` с необходимыми параметрами:
   ```env
   CB_URL=url
   TOKEN_TTL=10
   BOT_TOKEN=token
   ```
2. Запустите приложение:
   ```bash
   docker-compose up -d cb-bot-service
   ```
## CI/CD

Для автоматизации процессов сборки, тестирования и публикации используется **GitHub Actions**.

### Workflow: Build, Test, and Publish

#### Триггеры
- **Ручной запуск** через `workflow_dispatch` с параметрами:
    - `branch`: Ветка для сборки (по умолчанию `main`).
    - `skip_tests`: Пропустить тесты (по умолчанию `false`).
- **Автоматический запуск** при пуше в ветку `main`.

#### Основные этапы

1. **Сборка проекта**:
    - Настройка Java среды (JDK 17).
    - Кэширование зависимостей Maven.
    - Сборка проекта с использованием Maven:
      ```bash
      mvn clean package -DskipTests
      ```
    - Сохранение артефактов сборки (`*.jar`) в GitHub Actions.

2. **Запуск тестов**:
    - Скачивание ранее собранных артефактов.
    - Выполнение модульных тестов (если `skip_tests` не установлен в `true`):
      ```bash
      mvn test
      ```
    - Сохранение отчётов о тестах (`surefire-reports`).

3. **Публикация Docker-образа**:
    - Сборка Docker-образа с использованием последнего SHA коммита:
      ```bash
      docker build -t <docker_hub_username>/cb-bot-service:<commit_sha> .
      ```
    - Публикация образа в Docker Hub с тегами:
        - `<commit_sha>`
        - `latest`

---

### Workflow: Deploy

#### Триггеры
- **Ручной запуск** через `workflow_dispatch` с параметром:
    - `image_tag`: Тег Docker-образа для деплоя (по умолчанию `latest`).

#### Основные этапы

1. Настройка SSH-доступа:
    - Использование приватного ключа SSH для подключения к серверу.
    - Добавление сервера в `known_hosts` для безопасного подключения.

2. Остановка текущего контейнера:
    - Остановка и удаление запущенного контейнера `cb-bot-service`.

3. Обновление переменных окружения:
    - Обновление значения `CB_BOT_SERVICE_DOCKER_TAG` в файле `.env`.

4. Запуск нового контейнера:
    - Подтягивание нового Docker-образа из Docker Hub:
      ```bash
      docker pull <docker_hub_username>/cb-bot-service:<image_tag>
      ```
    - Перезапуск сервиса с использованием `docker-compose`:
      ```bash
      docker-compose up -d --build --no-deps cb-bot-service --remove-orphans
      ```

### Требования

Для корректной работы Workflows необходимо настроить следующие GitHub Secrets:

- **`DOCKER_HUB_USERNAME`**: Имя пользователя Docker Hub.
- **`DOCKER_HUB_PASSWORD`**: Пароль для Docker Hub.
- **`GH_TOKEN`**: Личный токен GitHub для работы с пакетами и API.
- **`GH_USERNAME`**: Имя пользователя GitHub.
- **`SERVER_IP`**: IP-адрес удалённого сервера для деплоя.
- **`SSH_PRIVATE_KEY`**: Приватный ключ для доступа к серверу через SSH.
- **`SSH_KNOWN_HOSTS`**: Список известных хостов для SSH.

И сервер как в [Server configuration](https://github.com/mastkeey/cb-server-config)
