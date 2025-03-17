# mstTituls

**mstTituls** — это плагин для Minecraft, который позволяет управлять титулами игроков с помощью удобного интерфейса и мощных интеграций.

## Особенности

- Полноценное управление титулами прямо в игре.
- Интеграция с PlaceholderAPI для отображения титулов.
- Использование базы данных MySQL и Redis для хранения данных и кэширования.
- Поддержка покупки титулов за очки через **PlayerPoints**.
- Гибкая настройка интерфейса меню.
- Расширенные команды и права доступа.

---

## Установка

1. Скачайте последнюю версию плагина из [релизов](https://github.com/mistaste/mstTituls/releases).
2. Поместите файл `mstTituls.jar` в папку `plugins` на вашем сервере.
3. Перезапустите сервер.
4. Настройте файл `config.yml` в директории `plugins/mstTituls`.

---

## Конфигурация

Пример стандартного файла конфигурации плагина `config.yml`:

```yaml
database:
  host: "localhost"
  port: 3306
  database: "mydatabase"
  username: "root"
  password: ""

redis:
  host: "127.0.0.1"
  port: 6379
  username: ""  # Если не используется, оставьте пустым
  password: ""  # Если не используется, оставьте пустым
  cache_enabled: true  # Включить/выключить использование Redis

settings:
  menu-title: "&#C9E4DEТитулы"
  menu-size: 54
  command: "luckperms user %player% meta setsuffix \"%titul%\""
  clear-command: "luckperms user %player% meta clear"
  price: 200
messages:
  prefix: "(!) "
  reload: "Плагин перезагружен"
  noperm: "У вас нет прав"
```

---

## Команды

- `/msttitul` – открыть меню титулов.
- `/msttitul reload` – перезагрузить конфигурацию плагина.

---

## Права Доступа

- `msttituls.use` – доступ для открытия меню титулов.
- `msttituls.admin` – доступ для выполнения административных команд, например, перезагрузки конфигурации.

---

## Для разработчиков

### API плагина

Плагин предоставляет API для взаимодействия с системой титулов:

```java
TitulManager titulManager = main.getInstance().getTitulManager();

// Получение титулов игрока
List<String> tituls = titulManager.getTituls(player.getName());

// Проверка, есть ли у игрока титулы
boolean hasTituls = titulManager.hasTituls(player.getName());

// Добавление титула игроку
titulManager.addTitul(player.getName(), "MyCustomTitle");

// Удаление титула игрока
titulManager.removeTitul(player.getName(), "MyCustomTitle");
```

---

### Сборка плагина из исходного кода

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/mistaste/mstTituls.git
   ```
2. Соберите проект с помощью Maven:
   ```bash
   mvn clean package
   ```
3. JAR-файл с плагином появится в папке `target`.

---

## Зависимости

Для корректной работы плагина требуются следующие зависимости:

- **PaperMC API** (1.20+).
- **PlaceholderAPI**.
- **PlayerPoints**.
- **Vault**.

---

## Благодарности

Разработка: [mistaste](https://github.com/mistaste)  
Лицензия: MIT  
Вопросы и предложения: [Issues](https://github.com/mistaste/mstTituls/issues)