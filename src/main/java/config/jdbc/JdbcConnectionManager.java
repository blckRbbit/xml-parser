package config.jdbc;

import util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(JdbcConnectionManager.class.getName());
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
            LOGGER.log(Level.WARNING, "Неверные данные для подключения к БД (проверьте хост, порт, пользователя и пароль для подключения к БД)");
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
            LOGGER.log(Level.WARNING, "Не подключен JDBC-драйвер");
            throw new RuntimeException(e);
        }
    }
}
