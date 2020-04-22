package io.todo.api.repository;

import io.todo.api.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    /**
     * Method to save a task
     * @param task
     */
    UUID persist(Task task);


    /**
     * Method to fetch all tasks created by user
     *
     * @param username
     * @param page
     * @param size
     * @return @{@link Optional<List<Task>>}
     */
    Optional<List<Task>> findAll(String username, Integer page, Integer size);

    /**
     * Method to fetch all incomplete tasks
     *
     * @param username
     * @param page
     * @param size
     * @return @{@link Optional<List<Task>>}
     */
    Optional<List<Task>> findInProgress(String username, Integer page, Integer size);

    /**
     * Method to fetch all completed tasks
     *
     * @param username
     * @param page
     * @param size
     * @return @{@link Optional<List<Task>>}
     */
    Optional<List<Task>> findCompleted(String username, Integer page, Integer size);

    /**
     * Method to fetch Task details by id
     * @param id
     */
    Task findById(UUID id);
}
