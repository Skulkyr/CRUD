package org.pogonin.crud.core.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.core.exceptions.TaskNotFoundException;
import org.pogonin.crud.core.repository.TaskRepository;
import org.pogonin.crud.web.dto.input.TaskDto;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto(
                "Test Task",
                "Test Description",
                "WAITING");
        task = Task.builder()
                .id(1)
                .title("Test Task")
                .description("Test Description")
                .status(Task.Status.WAITING)
                .build();
    }

    @Test
    void testSave() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);


        Task savedTask = taskService.save(taskDto);


        assertNotNull(savedTask);
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
        assertEquals(Task.Status.WAITING, savedTask.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testFindAll() {
        List<Task> tasks = Arrays.asList(
                task,
                Task.builder().id(2).title("Another Task").description("Another Description").status(Task.Status.IN_PROGRESS).build()
        );
        when(taskRepository.findAll()).thenReturn(tasks);
        
        
        List<Task> result = taskService.findAll();


        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(taskRepository.findById(1)).thenReturn(task);


        Task foundTask = taskService.findById(1);


        assertNotNull(foundTask);
        assertEquals(1, foundTask.getId());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testFindById_NotFound() {
        when(taskRepository.findById(1)).thenReturn(null);


        Task foundTask = taskService.findById(1);

        
        assertNull(foundTask);
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testDelete_Success() {
        when(taskRepository.delete(1)).thenReturn(1);

        
        assertDoesNotThrow(() -> taskService.delete(1));

        
        verify(taskRepository, times(1)).delete(1);
    }

    @Test
    void testDelete_NotFound() {
        when(taskRepository.delete(1)).thenReturn(0);

        
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.delete(1));

        
        assertEquals("Task with id 1 not found", exception.getMessage());
        verify(taskRepository, times(1)).delete(1);
    }

    @Test
    void testUpdate_Success() {
        TaskDto updatedDto = new TaskDto(
                "Updated Task",
                "Updated Description",
                "IN_PROGRESS");
        Task updatedTask = Task.builder()
                .id(1)
                .title("Updated Task")
                .description("Updated Description")
                .status(Task.Status.IN_PROGRESS)
                .build();
        when(taskRepository.update(any(Task.class))).thenReturn(1);
        when(taskRepository.findById(1)).thenReturn(updatedTask);

        
        Task result = taskService.update(1, updatedDto);

        
        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(Task.Status.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).update(any(Task.class));
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void testUpdate_NotFound() {
        TaskDto updatedDto = new TaskDto(
                "Updated Task",
                "Updated Description",
                "IN_PROGRESS");
        when(taskRepository.update(any(Task.class))).thenReturn(0);

        
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> taskService.update(1, updatedDto));

        
        assertEquals("Task with id 1 not found", exception.getMessage());
        verify(taskRepository, times(1)).update(any(Task.class));
        verify(taskRepository, times(0)).findById(anyInt());
    }
}
        

