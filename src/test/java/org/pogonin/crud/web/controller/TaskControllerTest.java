package org.pogonin.crud.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.core.exceptions.TaskNotFoundException;
import org.pogonin.crud.core.service.TaskService;
import org.pogonin.crud.web.dto.input.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetById_Success() throws Exception {
        Mockito.when(taskService.findById(1)).thenReturn(task);


        mockMvc.perform(get("/1")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(task.getId())))
                .andExpect(jsonPath("$.title", is(task.getTitle())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.status", is(task.getStatus().toString())));
        Mockito.verify(taskService, Mockito.times(1)).findById(1);
    }

    @Test
    void testGetById_NotFound() throws Exception {
        Mockito.when(taskService.findById(1)).thenThrow(new TaskNotFoundException("Task with id 1 not found"));


        mockMvc.perform(get("/1")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isBadRequest());


        Mockito.verify(taskService, Mockito.times(1)).findById(1);
    }

    @Test
    void testGetAll() throws Exception {
        List<Task> tasks = Arrays.asList(
                task,
                Task.builder().id(2).title("Another Task").description("Another Description").status(Task.Status.IN_PROGRESS).build()
        );
        Mockito.when(taskService.findAll()).thenReturn(tasks);


        mockMvc.perform(get("/all")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(task.getId())))
                .andExpect(jsonPath("$[0].title", is(task.getTitle())))
                .andExpect(jsonPath("$[0].description", is(task.getDescription())))
                .andExpect(jsonPath("$[0].status", is(task.getStatus().toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Another Task")))
                .andExpect(jsonPath("$[1].description", is("Another Description")))
                .andExpect(jsonPath("$[1].status", is("IN_PROGRESS")));

        Mockito.verify(taskService, Mockito.times(1)).findAll();
    }

    @Test
    void testCreate() throws Exception {
        Mockito.when(taskService.save(any(TaskDto.class))).thenReturn(task);


        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))


                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(task.getId())))
                .andExpect(jsonPath("$.title", is(task.getTitle())))
                .andExpect(jsonPath("$.description", is(task.getDescription())))
                .andExpect(jsonPath("$.status", is(task.getStatus().toString())));

        Mockito.verify(taskService, Mockito.times(1)).save(any(TaskDto.class));
    }

    @Test
    void testUpdate_Success() throws Exception {
        TaskDto updatedDto = new TaskDto(
                "Updated Task",
                "Updated Description",
                "IN_PROGRESS"
        );
        Task updatedTask = Task.builder()
                .id(1)
                .title("Updated Task")
                .description("Updated Description")
                .status(Task.Status.IN_PROGRESS)
                .build();
        Mockito.when(taskService.update(Mockito.eq(1), any(TaskDto.class))).thenReturn(updatedTask);

        
        mockMvc.perform(patch("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                
                
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedTask.getId())))
                .andExpect(jsonPath("$.title", is(updatedTask.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedTask.getDescription())))
                .andExpect(jsonPath("$.status", is(updatedTask.getStatus().toString())));
        
        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.eq(1), any(TaskDto.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        TaskDto updatedDto = new TaskDto(
                "Updated Task",
                "Updated Description",
                "IN_PROGRESS"
        );
        Mockito.when(taskService.update(Mockito.eq(1), any(TaskDto.class)))
                .thenThrow(new TaskNotFoundException("Task with id 1 not found"));

        
        mockMvc.perform(patch("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                
                
                .andExpect(status().isBadRequest());
        Mockito.verify(taskService, Mockito.times(1)).update(Mockito.eq(1), any(TaskDto.class));
    }

    @Test
    void testDelete_Success() throws Exception {
        doNothing().when(taskService).delete(1);
        
        
        mockMvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                
                
                .andExpect(status().isOk());
        
        Mockito.verify(taskService, Mockito.times(1)).delete(1);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        doThrow(new TaskNotFoundException("Task with id 1 not found")).when(taskService).delete(1);

        
        mockMvc.perform(delete("/1")
                        .contentType(MediaType.APPLICATION_JSON))
                
                
                .andExpect(status().isBadRequest());
        Mockito.verify(taskService, Mockito.times(1)).delete(1);
    }
}

