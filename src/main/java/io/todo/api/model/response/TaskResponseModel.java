package io.todo.api.model.response;

import io.todo.api.model.bo.TaskBO;

import java.util.List;

public class TaskResponseModel {
    private List<TaskBO> taskBOList;

    public List<TaskBO> getTaskBOList() {
        return taskBOList;
    }

    public void setTaskBOList(List<TaskBO> taskBOList) {
        this.taskBOList = taskBOList;
    }
}
