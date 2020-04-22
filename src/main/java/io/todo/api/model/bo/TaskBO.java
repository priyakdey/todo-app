package io.todo.api.model.bo;

import java.time.LocalDate;
import java.util.UUID;

public class TaskBO {
    private UUID id;
    private final String username;
    private final String title;
    private String description;
    private LocalDate startedOn;
    private LocalDate expectedCompletionBy;
    private LocalDate completedOn;
    private Boolean completed;


    public TaskBO(String username, String title) {
        this.username = username;
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartedOn() {
        return startedOn;
    }

    public LocalDate getExpectedCompletionBy() {
        return expectedCompletionBy;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Builder class for TaskBO
     */
    public static final class TaskBoBuilder {
        private TaskBO taskBO;

        public TaskBoBuilder(String username, String title) {
            taskBO = new TaskBO(username, title);
        }

        public static final TaskBoBuilder getInstance(String username, String title) {
            return new TaskBoBuilder(username, title);
        }

        public TaskBoBuilder description(String description) {
            taskBO.description = description;
            return this;
        }

        public TaskBoBuilder expectedCompletionBy(LocalDate expectedCompletionBy) {
            taskBO.expectedCompletionBy = expectedCompletionBy;
            return this;
        }

        public TaskBoBuilder completedOn(LocalDate completedOn) {
            taskBO.completedOn = completedOn;
            return this;
        }

        public TaskBoBuilder id(UUID id) {
            taskBO.id = id;
            return this;
        }

        public TaskBoBuilder startedOn(LocalDate startedOn) {
            taskBO.startedOn = startedOn;
            return this;
        }

        public TaskBO compose() {
            return taskBO;
        }
    }
}
