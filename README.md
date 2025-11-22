# Система рекрутинга

## Описание

REST API сервис для управления процессом подбора персонал, имеющий функционал от создания вакансий до проведения собеседований и выдачи офферов

## Стек технологий

- Java 21
- Spring Boot 3.5.7
- Postgresql

## Сущности


### Candidate (Кандидат)
- `id` - уникальный идентификатор
- `firstName` - имя
- `lastName` - фамилия  
- `email` - электронная почта (уникальная)
- `phone` - телефон
- `positionWanted` - желаемая позиция

### Vacancy (Вакансия)
- `id` - уникальный идентификатор
- `title` - заголовок вакансии
- `description` - описание
- `department` - департамент/отдел
- `salaryFrom` - зарплата «от»
- `salaryTo` - зарплата «до»
- `status` - статус: `DRAFT` | `PUBLISHED` | `CLOSED`

### Application (Заявка)
- `id` - уникальный идентификатор
- `candidateId` - ссылка на кандидата
- `vacancyId` - ссылка на вакансию
- `status` - статус: `CREATED` | `IN_REVIEW` | `INTERVIEW_SCHEDULED` | `REJECTED` | `OFFERED`

### Interview (Собеседование)
- `id` - уникальный идентификатор
- `applicationId` - ссылка на заявку
- `interviewerId` - идентификатор интервьюера
- `startTime` - время начала 
- `endTime` - время окончания 
- `location` - место проведения или платформа
- `status` - статус: `SCHEDULED` | `COMPLETED` | `CANCELED`

### Offer (Оффер)
- `id` - уникальный идентификатор
- `applicationId` - ссылка на заявку
- `compensation` - предлагаемая компенсация
- `status` - статус: `CREATED` | `SENT` | `ACCEPTED` | `REJECTED` | `EXPIRED`

### User (Пользователи)
- `id` - уникальный идентификатор
- `username` - имя пользователя
- `email` - почта пользователя
- `roles` - роль пользователя

### Login (аутентификация)
- `username` - имя пользователя
- `password` - пароль пользователя

### Registration (регистрация)
- `username` - имя пользователя
- `email` - почта пользователя
- `password` - пароль пользователя
- `passwordConfirm` - подтверждение пароля пользователя


## API-Endpoints

### Candidates
- `POST /api/candidates` — создать кандидата
- `GET /api/candidates` — получить всех кандидатов
- `GET /api/candidates/{id}` — получить кандидата по ID
- `PUT /api/candidates/{id}` — обновить кандидата
- `DELETE /api/candidates/{id}` — удалить кандидата

### Vacancies
- `POST /api/vacancies` — создать вакансию
- `GET /api/vacancies` — получить все вакансии
- `GET /api/vacancies/{id}` — получить вакансию по ID
- `PUT /api/vacancies/{id}` — обновить вакансию
- `DELETE /api/vacancies/{id}` — удалить вакансию
- `POST /api/vacancies/{id}/publish` — опубликовать вакансию
- `POST /api/vacancies/{id}/close` — закрыть вакансию

### Applications
- `POST /api/applications` — создать заявку
- `GET /api/applications` — получить все заявки
- `GET /api/applications/{id}` — получить заявку по ID
- `PUT /api/applications/{id}` — обновить заявку
- `DELETE /api/applications/{id}` — удалить заявку
- `GET /api/applications/candidate/{candidateId}` — заявки по кандидату
- `GET /api/applications/vacancy/{vacancyId}` — заявки по вакансии

### Interviews
- `POST /api/interviews` — создать собеседование
- `GET /api/interviews` — получить все собеседования
- `GET /api/interviews/{id}` — получить собеседование по ID
- `PUT /api/interviews/{id}` — обновить собеседование
- `DELETE /api/interviews/{id}` — удалить собеседование
- `POST /api/interviews/{id}/complete` — завершить собеседование

### Offers
- `POST /api/offers` — создать оффер
- `GET /api/offers` — получить все офферы
- `GET /api/offers/{id}` — получить оффер по ID
- `PUT /api/offers/{id}` — обновить оффер
- `DELETE /api/offers/{id}` — удалить оффер
- `POST /api/offers/{id}/accept` — принять оффер
- `POST /api/offers/{id}/reject` — отклонить оффер

### Auth
- `POST /api/auth/register` - регистрация пользователя
- `POST /api/auth/login` - авторизация пользователя
- `POST /api/auth/{userID}/assign-role/{roleName}` - выдать роль пользователю
- `POST /api/auth/{userID}/remove-role/{roleName}` - отозвать роль у пользователя

### Users (the admin role is required)
- `GET /api/users` - список пользователей
- `GET /api/users/{id}` - информ. пользователя по ID
- `PUT /api/users/{id}` - обновить данные пользователя
- `DELETE /api/users/{id}` - удалить пользователя

## Бизнес-операции

### 1. Публикация вакансии
- `POST /api/vacancies/{id}/publish`

**Функционал:** переводит вакансию из статуса `DRAFT` в `PUBLISHED`, открывая приём заявок


### 2. Создание заявки кандидата
- `POST /api/applications`

**Функционал:** создаёт заявку на вакансию со статусом `CREATED`


### 3. Планирование собеседования
- `POST /api/interviews`

**Функционал:** создаёт запись о собеседовании с проверкой доступности интервьюера

### 4. Завершение собеседования с созданием оффера
- `POST /api/interviews/{id}/complete`

**Функционал:** завершает собеседование и автоматически создаёт оффер при успешном результате

### 5. Принятие оффера кандидатом
- `POST /api/offers/{id}/accept`

**Функционал:** переводит оффер в статус `ACCEPTED`

### 6. Отклонение оффера
- `POST /api/offers/{id}/reject`

**Функционал:** переводит оффер в статус `REJECTED`

### 7. Закрытие вакансии
- `POST /api/vacancies/{id}/close`

**Функционал:** переводит вакансию в статус `CLOSED`, останавливая процесс найма

### 8. Управление ролями пользователя
- `POST /api/auth/{userID}/assign-role/{roleName}`
- `POST /api/auth/{userID}/remove-role/{roleName}`

**Функционал:** присвоение/отзыв ролей для разграничения доступа

### 9. Администрирование пользователей
- `PUT /api/users/{id}`
- `DELETE /api/users/{id}`

**Функционал:** обновление и удаление учетный записей 


## Бизнес-правила

- Один кандидат может подать только одну заявку на вакансию
- Публикация вакансии разрешена только из статуса `DRAFT`
- Интервьюер не может иметь пересекающихся собеседований
- Кандидат может принять только один оффер
- Завершение собеседования и создание оффера выполняются атомарно
- Операции с закрытыми вакансиями ограничены
- Доступы по ролям: управление вакансиями, интервью и офферами - HR/ADMIN; управление пользователями и ролями - ADMIN; кандидат видит и может редактировать свои данные, а также подавать заявки