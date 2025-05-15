package diary;

import utils.FileUtils;
import java.time.LocalDateTime;
import java.util.Scanner;
public class diary {
    private static final int MAX_RECORDS = 50;
    private LocalDateTime[] datesTimes = new LocalDateTime[MAX_RECORDS];
    private char[][] texts = new char[MAX_RECORDS][1000];
    private int recordCount = 0;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        System.out.println("Щоденник котика ПЕРЕРОБЛЕНО.");

        while (running) {
            System.out.println("\nОберіть дію яку зроблять котики :");
            System.out.println("1 - Котик додасть запис");
            System.out.println("2 - Котик видалить запис");
            System.out.println("3 - Переглянути всі записи котиків");
            System.out.println("0 - Вийти");
            System.out.print("Оберіть дію котика: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addEntry(scanner);
                    break;
                case "2":
                    deleteEntry(scanner);
                    break;
                case "3":
                    showEntries();
                    break;
                case "0":
                    running = false;
                    System.out.println("До побачення! Нехай ваші спогади залишаються теплими, як пухнастий котик на колінах.");
                    break;
                default:
                    System.out.println("Котик не знає що робити, таких команд ми ще не знаємо(.");
            }
        }
        scanner.close();
    }

    private void addEntry(Scanner scanner) {
        if (recordCount >= MAX_RECORDS) {
            System.out.println("Котик заповнив щоденник на максимум. Видаліть запис, щоб додати новий.");
            return;
        }

        int[] dateTimeParts = getDateTimeFromUser(scanner, "додавання");
        if (dateTimeParts == null) return;

        LocalDateTime dateTime = FileUtils.createDateTime(dateTimeParts[0], dateTimeParts[1], dateTimeParts[2],
                dateTimeParts[3], dateTimeParts[4], dateTimeParts[5]);

        System.out.println("Введіть текст запису (Enter на порожньому рядку для завершення):");
        int charIndex = 0;
        while (true) {
            String line = scanner.nextLine();
            if (line.isEmpty()) break;
            for (char c : line.toCharArray()) {
                if (charIndex < 999) {
                    texts[recordCount][charIndex++] = c;
                } else {
                    System.out.println("Запис занадто довгий. Котик не може все запам'ятати.");
                    return;
                }
            }
            if (charIndex < 999) texts[recordCount][charIndex++] = '\n';
        }
        texts[recordCount][charIndex] = '\0';

        datesTimes[recordCount++] = dateTime;
        System.out.println("Запис успішно додано.");
    }

    private void deleteEntry(Scanner scanner) {
        int[] dateTimeParts = getDateTimeFromUser(scanner, "видалення");
        if (dateTimeParts == null) return;

        LocalDateTime dateTimeToDelete = FileUtils.createDateTime(dateTimeParts[0], dateTimeParts[1], dateTimeParts[2],
                dateTimeParts[3], dateTimeParts[4], dateTimeParts[5]);

        for (int i = 0; i < recordCount; i++) {
            if (dateTimeToDelete.equals(datesTimes[i])) {
                for (int j = i; j < recordCount - 1; j++) {
                    datesTimes[j] = datesTimes[j + 1];
                    texts[j] = texts[j + 1].clone();
                }
                datesTimes[--recordCount] = null;
                texts[recordCount] = new char[1000];
                System.out.println("Запис видалено.");
                return;
            }
        }
        System.out.println("Запис за такою датою не знайдено.");
    }

    private void showEntries() {
        if (recordCount == 0) {
            System.out.println("Записів поки що немає.");
            return;
        }
        for (int i = 0; i < recordCount; i++) {
            System.out.println("Дата та час: " + datesTimes[i]);
            System.out.println(new String(texts[i]).trim());
        }
    }

    private int[] getDateTimeFromUser(Scanner scanner, String action) {
        int[] parts = new int[6];
        String[] prompts = {"рік (РРРР)", "місяць (ММ)", "день (ДД)", "годину (гг)", "хвилини (хх)", "секунди (сс)"};

        while (true) {
            boolean valid = true;
            for (int i = 0; i < 6; i++) {
                System.out.print("Введіть " + prompts[i] + " для " + action + ": ");
                if (scanner.hasNextInt()) {
                    parts[i] = scanner.nextInt();
                } else {
                    System.out.println("Некоректний формат. Спробуйте ще раз.");
                    scanner.next();
                    valid = false;
                    break;
                }
            }
            scanner.nextLine();
            if (valid && FileUtils.isValidDateTime(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5])) {
                return parts;
            }
            System.out.println("Некоректні дані. Спробуйте знову.");
        }
    }
}
