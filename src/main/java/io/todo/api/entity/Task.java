package io.todo.api.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String title;

    private String description;

    private Boolean completed = Boolean.FALSE;

    @Column(nullable = false, updatable = false)
    private LocalDate startedOn = LocalDate.now(ZoneId.systemDefault());

    @Column(updatable = false)
    private LocalDate expectedCompletionBy;

    private LocalDate completedOn;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name = "created_by", referencedColumnName = "username")
    private User user;

    public Task() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDate getStartedOn() {
        return startedOn;
    }

    public void setStartedOn(LocalDate startedOn) {
        this.startedOn = startedOn;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDate completedOn) {
        this.completedOn = completedOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getExpectedCompletionBy() {
        return expectedCompletionBy;
    }

    public void setExpectedCompletionBy(LocalDate expectedCompletionBy) {
        this.expectedCompletionBy = expectedCompletionBy;
    }
}
