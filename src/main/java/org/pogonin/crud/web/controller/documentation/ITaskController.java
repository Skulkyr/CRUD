package org.pogonin.crud.web.controller.documentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.web.dto.input.TaskDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Контроллер управления тасками", description = "Простой CRUD контроллер")
public interface ITaskController {


    @Operation(summary = "Получить пользователя по id",
            description = "Возвращает информацию о пользователе по его уникальному идентификатору")
    ResponseEntity<Task> getById(
            @Parameter(
                    name = "id",
                    description = "Уникальный идентификатор пользователя",
                    required = true) Integer id);



    @Operation(summary = "Получить список всех задач",
            description = "Возвращает все задачи")
    ResponseEntity<List<Task>> getAll();


    @Operation(summary = "Создать новую задачу",
            description = "Создает новую задачу и возвращает ее с присвоенным id")
    ResponseEntity<Task> create(TaskDto taskDto);


    @Operation(summary = "Обновить задачу",
            description = "Обновляет задачу и возвращает ее")
    ResponseEntity<Task> update(
            @Parameter(
                    name = "id",
                    description = "Уникальный идентификатор пользователя",
                    required = true) Integer id,
            TaskDto taskDto);


    @Operation(summary = "Удалить задачу",
            description = "Удаляет задачу из базы данных",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content()
            )})
    ResponseEntity<Task> delete(
            @Parameter(
                    name = "id",
                    description = "Уникальный идентификатор пользователя",
                    required = true) Integer id);
}
