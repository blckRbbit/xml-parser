package config.jdbc;

import util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionManager {
    static {
        loadDriver();
    }

    private JdbcConnectionManager() {}

    /**
     * Соединение с базой данных
     * @return
     * @throws SQLException может произойти, если приложение подключается к несуществующей базе, или неверно указаны пользователь или пароль.
     */
    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    Util.getUrl(),
                    Util.getUsername(),
                    Util.getPassword()
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
            Class.forName(Util.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
