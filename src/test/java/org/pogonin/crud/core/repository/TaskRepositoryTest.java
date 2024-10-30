package org.pogonin.crud.core.repository;


import org.junit.jupiter.api.Test;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.core.exceptions.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TaskRepository.class)
public class TaskRepositoryTest {

    @Autowired
    TaskRepository repository;


    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");


    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }


    @Test
    @Transactional
    void testSave() {
        Task task = Task.builder()
                .title("Test Task")
                .description("Description")
                .status(Task.Status.WAITING)
                .build();

        Task savedTask = repository.save(task);

        assertNotNull(savedTask.getId());
        assertNotNull(savedTask.getCreated());
        assertNotNull(savedTask.getUpdated());
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Description", savedTask.getDescription());
        assertEquals(Task.Status.WAITING, savedTask.getStatus());
    }

    @Test
    @Transactional
    void testFindById() {
        Task task = Task.builder()
                .title("Test Task")
                .description("Description")
                .status(Task.Status.IN_PROGRESS)
                .build();

        Task savedTask = repository.save(task);

        Task foundTask = repository.findById(savedTask.getId());

        assertEquals(savedTask.getId(), foundTask.getId());
        assertEquals("Test Task", foundTask.getTitle());
        assertEquals("Description", foundTask.getDescription());
        assertEquals(Task.Status.IN_PROGRESS, foundTask.getStatus());
    }

    @Test
    @Transactional
    void testFindAll() {
        Task task1 = Task.builder()
                .title("Task 1")
                .description("Description 1")
                .status(Task.Status.WAITING)
                .build();

        Task task2 = Task.builder()
                .title("Task 2")
                .description("Description 2")
                .status(Task.Status.DONE)
                .build();

        repository.save(task1);
        repository.save(task2);

        List<Task> tasks = repository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    @Transactional
    void testUpdate() {
        Task task = Task.builder()
                .title("Old Title")
                .description("Old Description")
                .status(Task.Status.WAITING)
                .build();

        Task savedTask = repository.save(task);

        savedTask.setTitle("New Title");
        savedTask.setDescription("New Description");
        savedTask.setStatus(Task.Status.DONE);

        Task updatedTask = repository.save(savedTask);

        assertEquals("New Title", updatedTask.getTitle());
        assertEquals("New Description", updatedTask.getDescription());
        assertEquals(Task.Status.DONE, updatedTask.getStatus());
    }

    @Test
    @Transactional
    void testDelete() {
        Task task = Task.builder()
                .title("Task to Delete")
                .description("Description")
                .status(Task.Status.WAITING)
                .build();

        Task savedTask = repository.save(task);
        repository.delete(savedTask.getId());

        assertThrows(TaskNotFoundException.class, () -> repository.findById(savedTask.getId()));
    }
}
