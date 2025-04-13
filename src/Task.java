import java.time.Instant;
import java.util.Objects;
import java.time.format.DateTimeParseException;


public class Task {
    private String name;
    private Instant timestamp;
    private boolean completed = false;

    public Task(String name, Instant timestamp, boolean completed) {
        this.name = name;
        this.timestamp = timestamp;
        this.completed = completed;
    }

    public Task(String taskLine) {
        String[] Splitted = taskLine.split("_@");
        if (Splitted.length == 3) {
            this.name = Splitted[0];
            try {
                this.timestamp = Instant.parse(Splitted[1]);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid timestamp format in task line: " + taskLine, e);
            }
            this.completed = Objects.equals(Splitted[2], "true");
        } else {
            throw new IllegalArgumentException("Invalid task line format: " + taskLine);
        }
    }

    public String getName() {
        return name;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String asDBEntry() {
        return this.name + "_@" + this.timestamp.toString() + "_@" + this.completed;
    }
}
