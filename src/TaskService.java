import java.time.Instant;
import java.util.List;


class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void createTask(String name, Instant timestamp) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty.");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Task timestamp cannot be null.");
        }
        taskRepository.addTask(new Task(name, timestamp, false));
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public void markTaskCompleted(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative: " + index);
        }
        List<Task> tasks = taskRepository.getAllTasks();
        if (index < tasks.size()) {
            Task task = tasks.get(index);
            task.setCompleted(true);
            if (!taskRepository.updateTask(index, task)) {
                throw new IllegalStateException("Failed to update task at index: " + index);
            }
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " (size: " + tasks.size() + ")");
        }
    }

    public void rescheduleTask(int index, Instant timestamp) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative: " + index);
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null.");
        }
        List<Task> tasks = taskRepository.getAllTasks();
        if (index < tasks.size()) {
            Task task = tasks.get(index);
            task.setTimestamp(timestamp);
            if (!taskRepository.updateTask(index, task)) {
                throw new IllegalStateException("Failed to update task at index: " + index);
            }
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " (size: " + tasks.size() + ")");
        }
    }

    public void deleteTask(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be negative: " + index);
        }
        List<Task> tasks = taskRepository.getAllTasks();
        if (index < tasks.size()) {
            if (!taskRepository.deleteTask(index)) {
                throw new IllegalStateException("Failed to delete task at index: " + index);
            }
        }
        else{
            throw new IndexOutOfBoundsException("Index out of bounds: " + index + " (size: " + tasks.size() + ")");
        }
    }
}
