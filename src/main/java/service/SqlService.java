package service;

import config.jdbc.JdbcConnectionManager;
import parser.XmlParser;
import util.Util;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * В классе реализована логика работы приложения.
 */
public class SqlService {
    private static final Logger LOGGER = Logger.getLogger(SqlService.class.getName());

    private SqlService() {}

    /**
     * Основной старт-метод приложения, последовательно вызывает методы реализующие логику подключения к БД,
     * создания таблицы, парсинга xml, вставки в таблицу данных из файла, вывод результата.
     *
     * @throws SQLException может произойти, если приложение подключается к несуществующей базе,
     * или неверно указаны пользователь или пароль.
     */
    public static void run(String path, String tag) {
        try(Connection connection = JdbcConnectionManager.open()) {
            LOGGER.log(Level.INFO, "Подключение к базе данных прошло успешно");
            createTable(connection, Util.createMoviesTableProperty());
            fastInsert(Objects.requireNonNull(XmlParser.getRows(path, tag)), connection);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Ошибка при подключении к базе данных.");
        }
    }

    /**
     * Метод создания таблиц в базе данных, если они ещё не существуют
     * @throws SQLException может произойти, если приложение подключается к несуществующей базе,
     * или неверно указаны пользователь или пароль.
     */
    private static void createTable(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            LOGGER.log(Level.INFO, "Таблица успешно создана");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Ошибка при создании таблицы.");
        }
    }

    /**
     * Метод быстрой вставки данных в таблицы sql
     * @param rows - список содержащий массивы строк, которые являются результатами парсинга xml-файла.
     * @param connection - инстанс подключения к базе данных.
     * @throws SQLException может произойти, если приложение подключается к несуществующей базе,
     * при наличии ошибки в пользовательском sql-запросе,
     * или неверно указаны пользователь или пароль.
     */
    private static void fastInsert(List<String[]> rows, Connection connection) {
        try {
            PreparedStatement preparedStatementMovies = connection.prepareStatement(Util.insertToMoviesTable());
            for (String[] s: rows)  {
                preparedStatementMovies.setString(1, s[0]);
                preparedStatementMovies.setString(2, s[1]);
                preparedStatementMovies.setString(3, s[2]);
                preparedStatementMovies.addBatch();
            }
            preparedStatementMovies.executeBatch();
            LOGGER.log(Level.INFO, "Данные успешно добавлены в таблицу.");
            getCountRowsInTable(Util.getCountRows(), connection);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Ошибка при быстрой вставке в таблицу sql.");
        }
    }

    /**
     * Метод получения количества записей в таблице.
     * @param sql - строка с sql-скриптом
     * @param connection - инстанс подключения к базе данных.
     * @throws SQLException может произойти при наличии ошибки в пользовательском sql-запросе
     */
    private static void getCountRowsInTable(String sql, Connection connection) {
        try(ResultSet resultSet = connection.createStatement().executeQuery(sql)) {
            while (resultSet.next()) {
                LOGGER.log(Level.INFO, String.format("Кол-во записей в таблице: %s", resultSet.getString(1)));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Ошибка при подсчете строк в sql-таблице.");
        }
    }
}
