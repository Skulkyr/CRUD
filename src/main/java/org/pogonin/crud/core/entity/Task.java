package org.pogonin.crud.core.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "Сущность задачи")
public class Task implements Serializable {
    @Schema(description = "Id задачи", example = "1")
    private Integer id;
    @Schema(description = "Заголовок задачи", example = "Поспать")
    private String title;
    @Schema(description = "Подробное описание задачи", example = "Ну очень хочется")
    private String description;
    @Schema(description = "Статус задачи", example = "WAITING")
    private Status status;
    @Schema(description = "Время создания")
    private LocalDateTime created;
    @Schema(description = "Время последнего изменения")
    private LocalDateTime updated;

    public enum Status {WAITING, IN_PROGRESS, DONE}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return new EqualsBuilder()
                .append(id, task.id)
                .append(title, task.title)
                .append(description, task.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id).append(title)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("title", title)
                .append("description", description)
                .append("status", status)
                .append("created", created)
                .append("updated", updated)
                .toString();
    }
}
