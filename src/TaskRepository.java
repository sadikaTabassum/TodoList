import java.time.Instant;
import java.util.List;


interface TaskRepository {
    List<Task> getAllTasks();
    void addTask(Task task);
    boolean updateTask(int index, Task task);
    boolean deleteTask(int index);
    void saveTasks();
}
