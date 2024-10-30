package org.pogonin.crud.core.service;

import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.web.dto.input.TaskDto;

import java.util.List;

public interface TaskService {

    Task save(TaskDto taskDto);

    List<Task> findAll();


    Task findById(int id);

    void delete(int id);

    Task update(Integer id, TaskDto taskDto);
}
