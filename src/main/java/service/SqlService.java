package service;

import config.jdbc.JdbcConnectionManager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SqlService {
    private static final Logger LOGGER = Logger.getLogger(SqlService.class.getName());
    private static final Properties PROPERTIES = new Properties();
    private static final String CREATE_DIRECTORS_TABLE_KEY = "sql.table.directors.create";
    private static final String CREATE_MOVIES_TABLE_KEY = "sql.table.movies.create";

    static {
        loadProperties();
    }

    private SqlService() {}

    /**
     * Возвращает значение из property-файла по ключу key.
     * @param key
     * @return
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Метод подключения к базе данных
     * @throws SQLException может произойти, если приложение подключается к несуществующей базе, или неверно указаны пользователь или пароль.
     */
    public static void connectToDb() {
        try(Connection connection = JdbcConnectionManager.open()) {
            LOGGER.log(Level.INFO, "Подключение к базе данных прошло успешно");
            createTable(connection, get(CREATE_DIRECTORS_TABLE_KEY));
            createTable(connection, get(CREATE_MOVIES_TABLE_KEY));
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, String.format("Ошибка при подключении к базе данных: %n %s",  e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод создания таблиц в базе данных, если они ещё не существуют
     * @throws SQLException может произойти, если приложение подключается к несуществующей базе, неверно указаны пользователь или пароль, ошибка в sql-запросе.
     */
    private static void createTable(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            LOGGER.log(Level.INFO, "Таблица успешно создана");
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, "Ошибка при создании таблицы: ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод получения значений из файла application.properties
     * @throws IOException может произойти,
     * если файл с настройками не существует,
     * либо путь к файлу указан неверно.
     */
    private static void loadProperties() {
        try (InputStream inputStream = SqlService.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Проверьте правильность пути к файлу настроек приложения", e);
            throw new RuntimeException(e);
        }
    }
}
