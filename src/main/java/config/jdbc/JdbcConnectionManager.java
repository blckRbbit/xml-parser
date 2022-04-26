package config.jdbc;

import util.UtilPostgreSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionManager {
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
                    UtilPostgreSql.get(URL_KEY),
                    UtilPostgreSql.get(USERNAME_KEY),
                    UtilPostgreSql.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Загрузка нужного драйвера для базы данных
     * @throws ClassNotFoundException может произойти, если не подключена зависимость к нужному драйверу.
     */
    private static void loadDriver() {
        try {
            Class.forName(UtilPostgreSql.get(DRIVER_KEY));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
