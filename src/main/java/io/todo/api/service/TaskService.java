package io.todo.api.service;

import io.todo.api.model.bo.TaskBO;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    UUID saveNewTask(TaskBO taskBO);

    List<TaskBO> getTaskByUsername(String username, String filter, Integer page, Integer size);

    void markCompleted(UUID id);
}
