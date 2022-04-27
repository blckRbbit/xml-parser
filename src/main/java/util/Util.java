package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Утильный класс, с ключами для значений из property-файла и методами получения этих значений.
 */
public class Util {
    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());
    private static final Properties PROPERTIES = new Properties();
    private static final String SEPARATOR_KEY = "app.separator";
    private static final String CREATE_MOVIES_TABLE_KEY = "sql.table.movies.create";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driver";
    private static final String INSERT_TO_MOVIES_TABLE_KEY = "sql.table.movies.insert";
    private static final String COUNT_ROWS_KEY = "sql.table.movies.rows.count";

    static {
        loadProperties();
    }

    public static String getSeparator() {
        return get(SEPARATOR_KEY);
    }

    public static String createMoviesTableProperty() {
        return get(CREATE_MOVIES_TABLE_KEY);
    }

    public static String getUrl() {
        return get(URL_KEY);
    }

    public static String getUsername() {
        return get(USERNAME_KEY);
    }

    public static String getPassword() {
        return get(PASSWORD_KEY);
    }

    public static String getDriver() {
        return get(DRIVER_KEY);
    }

    public static String insertToMoviesTable() { return get(INSERT_TO_MOVIES_TABLE_KEY); }

    public static String getCountRows() { return get(COUNT_ROWS_KEY); }

    /**
     * Метод получения значений из файла application.properties
     * @throws IOException может произойти, если файл с настройками не существует,
     * либо путь к файлу указан неверно.
     */
    private static void loadProperties() {
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
            getProperties().load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Проверьте правильность пути к файлу настроек приложения: ", e);
        }
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }

    /**
     * Возвращает значение property по ключу
     * @param key
     * @return
     */
    private static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
