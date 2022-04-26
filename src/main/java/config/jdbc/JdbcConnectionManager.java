package config.jdbc;

import service.SqlService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcConnectionManager {
    private static final Logger LOGGER = Logger.getLogger(JdbcConnectionManager.class.getName());
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.user";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driver";

    static {
        loadDriver();
    }

    private JdbcConnectionManager() {}

    /**
     * Соединение с базой данных
     * @return
     * @throws SQLException может произойти,
     * если приложение подключается к несуществующей базе,
     * или неверно указаны пользователь или пароль.
     */
    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    SqlService.get(URL_KEY),
                    SqlService.get(USERNAME_KEY),
                    SqlService.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, String.format("Ошибка при подключении к базе данных: %n %s",  e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Загрузка нужного драйвера для базы данных
     * @throws ClassNotFoundException может произойти,
     * если не подключена зависимость к нужному драйверу.
     */
    private static void loadDriver() {
        try {
            Class.forName(SqlService.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
