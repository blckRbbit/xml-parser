package interfaces;

import service.SqlService;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserInterface {
    private static final Logger LOGGER = Logger.getLogger(UserInterface.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        LOGGER.log(Level.INFO, "Чтобы начать, нажмите Enter...");
        while (!scanner.nextLine().equalsIgnoreCase("q")) {
            LOGGER.log(Level.INFO, String.format("Введите путь к файлу: %n"));
            String path = scanner.nextLine();
            LOGGER.log(Level.INFO, String.format("Введите тэг для поиска сущности для парсинга: %n"));
            String tag = scanner.nextLine();
            SqlService.run(path, tag);
            LOGGER.log(Level.INFO, "Для продолжения нажмите Enter, для выхода - q...");
        }

    }
}
