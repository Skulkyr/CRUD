package org.pogonin.crud.web.controller;

import lombok.RequiredArgsConstructor;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.core.service.TaskService;
import org.pogonin.crud.web.controller.documentation.ITaskController;
import org.pogonin.crud.web.dto.input.TaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController implements ITaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Task> getById(@PathVariable Integer id) {
        var task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/all")
    @Override
    public ResponseEntity<List<Task>> getAll() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping()
    @Override
    public ResponseEntity<Task> create(@RequestBody TaskDto taskDto) {
        var task = taskService.save(taskDto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<Task> update(@PathVariable Integer id, @RequestBody TaskDto taskDto) {
        var task = taskService.update(id, taskDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Task> delete(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
