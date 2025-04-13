import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class TaskFileRepository implements TaskRepository {
    private final File DBFile = new File("tasks.db");
    private final List<Task> DBEntries = new ArrayList<>();

    public TaskFileRepository() {
        initDb();
    }

    private void initDb() {
        if (!DBFile.exists()) {
            try {
                if (DBFile.createNewFile()) {
                    System.out.println("DB created!");
                } else {
                    System.out.println("DB not created!");
                    throw new IOException("Failed to create database file.");
                }
            } catch (IOException e) {
                System.err.println("DB file not created: " + e.getMessage());
                throw new RuntimeException("Failed to initialize database.", e);
            }
        }
        readDb();
    }

    private void readDb() {
        if (DBFile.exists() && DBFile.canRead()) {
            try {
                DBEntries.clear();
                for (String e : Files.readAllLines(DBFile.toPath())) {
                    DBEntries.add(new Task(e));
                }
            } catch (IOException e) {
                System.err.println("Error reading database: " + e.getMessage());
                throw new RuntimeException("Failed to read database.", e);
            }
        } else if (!DBFile.exists()) {
            System.out.println("Database file does not exist.  It will be created on first write.");
        } else {
            System.err.println("Database file cannot be read.");
            throw new RuntimeException("Database file cannot be read.");
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(DBEntries);
    }

    @Override
    public void addTask(Task task) {
        DBEntries.add(task);
        saveTasks();
    }

    @Override
    public boolean updateTask(int index, Task task) {
        if (index >= 0 && index < DBEntries.size()) {
            DBEntries.set(index, task);
            saveTasks();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTask(int index) {
        if (index >= 0 && index < DBEntries.size()) {
            DBEntries.remove(index);
            saveTasks();
            return true;
        }
        return false;
    }

    @Override
    public void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.DBFile))) {
            for (Task t : DBEntries) {
                writer.write(t.asDBEntry());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
            throw new RuntimeException("Failed to save database.", e);
        }
    }
}
