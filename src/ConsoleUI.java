import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;


class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ZoneOffset zone = ZoneOffset.of("+06:00");

    public int displayMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Create");
        System.out.println("2. List");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();
            return -1;
        }
    }

    public String promptForTaskName() {
        System.out.print("Enter task name: ");
        return scanner.nextLine();
    }

    public Instant scanTimestamp() {
        Instant t = null;
        System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
        while (t == null) {
            String dateTimeInput = scanner.nextLine();
            try {
                t = LocalDateTime.parse(dateTimeInput, dtformatter).toInstant(zone);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm.");
            }
        }
        return t;
    }

    public int displayTaskList(List<Task> tasks) {
        System.out.println("\nTasks:");
        if (tasks.isEmpty()) {
            System.out.println("No tasks yet.");
            return 0;
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            System.out.println((i + 1) + ".\t" + t.getTimestamp().toString() + "\t" + t.getName()
                    + (t.isCompleted() ? " [Completed]" : ""));
        }

        int choice = -1;
        while (choice == -1) {
            System.out.print("Choose a task number for actions or 0 to go back: ");
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > tasks.size()) {
                    System.out.println("Invalid task number. Please enter a number between 0 and " + tasks.size() + ":");
                    choice = -1;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number:");
                scanner.next();
            }
            scanner.nextLine();
        }
        return choice;
    }

    public int displayTaskActionsMenu() {
        System.out.println("\nChoose an action:");
        System.out.println("1. Delete");
        System.out.println("2. Mark Completed");
        System.out.println("3. Reschedule");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
        int choice = -1;
        while (choice == -1) {
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice > 3) {
                    System.out.println("Invalid action. Please enter 0, 1, 2, or 3:");
                    choice = -1;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number:");
                scanner.next();
            }
            scanner.nextLine();
        }
        return choice;
    }

    public void displayTaskCreatedMessage() {
        System.out.println("Task Created!");
    }

    public void displayTaskDeletedMessage() {
        System.out.println("Task Deleted!");
    }

    public void displayTaskCompletedMessage() {
        System.out.println("Task Marked Completed!");
    }

    public void displayTaskRescheduledMessage() {
        System.out.println("Task Rescheduled!");
    }

    public void closeScanner() {
        scanner.close();
    }
}
