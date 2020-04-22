package io.todo.api.controller;

import io.todo.api.model.bo.TaskBO;
import io.todo.api.model.request.NewTaskModel;
import io.todo.api.model.response.TaskResponseModel;
import io.todo.api.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static io.todo.api.model.bo.TaskBO.TaskBoBuilder;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/users/{username}/tasks", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PreAuthorize(value = "#username == authentication.name")
    @PostMapping
    public ResponseEntity<?> addNewTask(@PathVariable String username, @RequestBody NewTaskModel model) {
        TaskBO taskBO = generateTaskBO(username, model);

        UUID id = taskService.saveNewTask(taskBO);

        try {
            return ResponseEntity
                    .created(new URI("/users/" + username + "/" + id.toString()))
                    .build();
        } catch (URISyntaxException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @GetMapping
    public ResponseEntity<TaskResponseModel> getTasksList
            (@PathVariable String username,
             @RequestParam(value = "filter", defaultValue = "incomplete") String filter,
             @RequestParam(value = "page", defaultValue = "1") Integer page,
             @RequestParam(value = "size", defaultValue = "10") Integer size) {

        List<TaskBO> taskBOList = taskService.getTaskByUsername(username, filter, page, size);

        TaskResponseModel response = new TaskResponseModel();
        response.setTaskBOList(taskBOList);

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> markCompleted(@PathVariable UUID id) {
        taskService.markCompleted(id);
        return ResponseEntity.noContent().build();
    }



    private TaskBO generateTaskBO(String username, NewTaskModel model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate expectedCompletionBy = LocalDate.parse(model.getExpectedCompletionBy(), formatter);

        return TaskBoBuilder.getInstance(username, model.getTitle())
                                    .description(model.getDescription())
                                    .expectedCompletionBy(expectedCompletionBy)
                                    .compose();
    }
}
