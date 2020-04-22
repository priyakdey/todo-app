package io.todo.api.service.impl;

import io.todo.api.entity.Task;
import io.todo.api.entity.User;
import io.todo.api.model.bo.TaskBO;
import io.todo.api.model.bo.TaskBO.TaskBoBuilder;
import io.todo.api.repository.TaskRepository;
import io.todo.api.repository.UserRepository;
import io.todo.api.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UUID saveNewTask(TaskBO taskBO) {
        Task task = createTask(taskBO);

        // Fetch the user details
        User user = userRepository.fetchTaskList(taskBO.getUsername());
        task.setUser(user);
        user.addTask(task);

        return taskRepository.persist(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskBO> getTaskByUsername(String username, String filter, Integer page, Integer size) {
        Optional<List<Task>> optional = null;
        switch (filter) {
            case "all":
                optional = taskRepository.findAll(username, page, size);
                break;
            case "completed":
                optional = taskRepository.findCompleted(username, page, size);
                break;
            case "incomplete":
            default:
                optional = taskRepository.findInProgress(username, page, size);
        }

        List<Task> taskList = optional.orElse(new ArrayList<>());
        return getTaskBOLIst(taskList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markCompleted(UUID id) {
        Task task = taskRepository.findById(id);
        task.setCompleted(Boolean.TRUE);
        task.setCompletedOn(LocalDate.now(Clock.systemDefaultZone()));
    }

    private List<TaskBO> getTaskBOLIst(List<Task> taskList) {
        return taskList.stream()
                    .map(this::mapToBO)
                    .collect(Collectors.toList());
    }

    private TaskBO mapToBO(Task task) {
        TaskBO taskBO = TaskBoBuilder.getInstance(task.getUser().getUsername(), task.getTitle())
                            .id(task.getId())
                            .description(task.getDescription())
                            .startedOn(task.getStartedOn())
                            .expectedCompletionBy(task.getExpectedCompletionBy())
                            .completedOn(task.getCompletedOn())
                            .compose();

        taskBO.setCompleted(task.getCompleted());
        return taskBO;
    }

    private Task createTask(TaskBO taskBO) {
        Task task = new Task();
        task.setTitle(taskBO.getTitle());
        task.setDescription(taskBO.getDescription());
        task.setExpectedCompletionBy(taskBO.getExpectedCompletionBy());
        return task;
    }
}
