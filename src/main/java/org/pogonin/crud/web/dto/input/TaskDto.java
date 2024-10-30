package org.pogonin.crud.web.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Транспортный объект для работы с задачами")
public class TaskDto {
    @Schema(description = "Заголовок задачи", example = "Поспать")
    private String title;
    @Schema(description = "Подробное описание задачи", example = "Ну очень хочется")
    private String description;
    @Schema(description = "Статус задачи", allowableValues = {"WAITING", "IN_PROGRESS", "DONE"})
    private String status;
}
