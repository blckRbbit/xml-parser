package interfaces;

import service.SqlService;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserInterface {
    private static final Logger LOGGER = Logger.getLogger(UserInterface.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        do {
            LOGGER.log(Level.INFO, String.format("Введите путь к файлу: %n"));
            String path = scanner.nextLine();
            LOGGER.log(Level.INFO, String.format("Введите тэг для поиска сущности для парсинга: %n"));
            String tag = scanner.nextLine();
            if (scanner.nextLine().equalsIgnoreCase("q")) {
                break;
            } else {
                SqlService.run(path, tag);
            }
        } while (true);

        }
}
