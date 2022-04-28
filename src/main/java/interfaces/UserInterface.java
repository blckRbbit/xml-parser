package interfaces;

import service.SqlService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс консольного пользовательского интерфейса
 */
public class UserInterface {
    private static final Logger LOGGER = Logger.getLogger(UserInterface.class.getName());

    /**
     * Метод обработки пользовательского ввода в консоли/терминале.
     */
    public static void run() {
        LOGGER.log(Level.INFO, "Чтобы начать, нажмите Enter...");
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));) {
            while (!reader.readLine().equalsIgnoreCase("q")) {
                LOGGER.log(Level.INFO,"Введите путь к файлу: %n");
                String path = reader.readLine();
                if (path==null || path.isEmpty()) {
                    LOGGER.log(Level.INFO, "Вы ввели пустую строку(для продолжения нажмите Enter, для выхода - q)");
                    continue;
                }
                LOGGER.log(Level.INFO, String.format(Locale.forLanguageTag("RU"),"Введите тэг для поиска сущности для парсинга: %n"));
                String tag = reader.readLine();
                if (tag==null || tag.isEmpty()) {
                    LOGGER.log(Level.INFO, "Вы ввели пустую строку(для продолжения нажмите Enter, для выхода - q)");
                    continue;
                }
                SqlService.run(path, tag);
                LOGGER.log(Level.INFO,"Для продолжения нажмите Enter, для выхода - q...");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Не удалось найти файл по указанному пути.");
        }

    }
}
