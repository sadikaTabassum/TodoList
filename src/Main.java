import java.time.Instant;
import java.util.List;

public class Main {
    private final ConsoleUI ui = new ConsoleUI();
    private final TaskService taskService = new TaskService(new TaskFileRepository());

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        try {
            while (true) {
                int choice = ui.displayMenu();
                if (choice == 0) {
                    exitApplication();
                    return;
                } else if (choice == -1) {
                    continue;
                } else if (choice == 1) {
                    createTask();
                } else if (choice == 2) {
                    listTasks();
                }
            }
        } catch (RuntimeException e) {
            System.err.println("An error occurred: " + e.getMessage());

        } finally {
            ui.closeScanner();
        }
    }

    private void createTask() {
        String name = ui.promptForTaskName();
        Instant timestamp = ui.scanTimestamp();
        try {
            taskService.createTask(name, timestamp);
            ui.displayTaskCreatedMessage();
        } catch (IllegalArgumentException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }

    }

    private void listTasks() {
        List<Task> tasks = taskService.getAllTasks();
        int selected = ui.displayTaskList(tasks);
        if (selected > 0 && selected <= tasks.size()) {
            performTaskActions(selected - 1);
        } else if (selected == -1) {

        }
    }

    private void performTaskActions(int selectedIndex) {
        int action = ui.displayTaskActionsMenu();
        switch (action) {
            case 1:
                try {
                    taskService.deleteTask(selectedIndex);
                    ui.displayTaskDeletedMessage();
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    System.err.println("Error deleting task: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    taskService.markTaskCompleted(selectedIndex);
                    ui.displayTaskCompletedMessage();
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    System.err.println("Error marking task completed: " + e.getMessage());
                }
                break;
            case 3:
                Instant newTimestamp = ui.scanTimestamp();
                try {
                    taskService.rescheduleTask(selectedIndex, newTimestamp);
                    ui.displayTaskRescheduledMessage();
                } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
                    System.err.println("Error rescheduling task: " + e.getMessage());
                }
                break;
            case 0:
                break;
            case -1:
                break;
            default:
                System.out.println("Invalid action. Please try again.");
        }
    }

    private void exitApplication() {
        System.out.println("Exiting application.");

    }
}
