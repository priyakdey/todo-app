package io.todo.api.model.request;

public class NewTaskModel {
    private String title;
    private String description;
    private String expectedCompletionBy;

    public NewTaskModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpectedCompletionBy() {
        return expectedCompletionBy;
    }

    public void setExpectedCompletionBy(String expectedCompletionBy) {
        this.expectedCompletionBy = expectedCompletionBy;
    }
}
