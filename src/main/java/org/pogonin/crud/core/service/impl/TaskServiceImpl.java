package org.pogonin.crud.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.core.exceptions.TaskNotFoundException;
import org.pogonin.crud.core.repository.TaskRepository;
import org.pogonin.crud.core.service.TaskService;
import org.pogonin.crud.web.dto.input.TaskDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Task save(TaskDto taskDto) {
        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(Task.Status.valueOf(taskDto.getStatus()))
                .build();

        return taskRepository.save(task);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    @Cacheable(value = "taskId", key = "#id")
    public Task findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    @CacheEvict(value = "taskId", key = "#id")
    public void delete(int id) {
        if(taskRepository.delete(id) == 0) {
            var message = "Task with id " + id + " not found";
            log.error(message);
            throw new TaskNotFoundException(message);
        }
    }

    @Override
    @CachePut(value = "taskId", key = "#id")
    public Task update(Integer id, TaskDto taskDto) {
        Task task = Task.builder()
                .id(id)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(Task.Status.valueOf(taskDto.getStatus()))
                .build();

        if(taskRepository.update(task) == 0) {
            var message = "Task with id " + id + " not found";
            log.error(message);
            throw new TaskNotFoundException(message);
        }

        return taskRepository.findById(id);
    }
}
