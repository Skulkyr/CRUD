package org.pogonin.crud.core.repository;

import lombok.extern.log4j.Log4j2;
import org.pogonin.crud.core.entity.Task;
import org.pogonin.crud.core.exceptions.SaveTaskException;
import org.pogonin.crud.core.exceptions.TaskNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Repository
public class TaskRepository {
    private final SimpleJdbcInsert insert;
    private final JdbcTemplate template;
    private final RowMapper<Task> ROW_MAPPER = new TaskRowMapper();

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("task")
                .usingGeneratedKeyColumns("id");
    }

    public Task save(Task task) {
        var now = LocalDateTime.now();

        task.setCreated(now);
        task.setUpdated(now);

        var params = new MapSqlParameterSource();
        params.addValue("title", task.getTitle());
        params.addValue("description", task.getDescription());
        params.addValue("status", task.getStatus().name(), Types.OTHER);
        params.addValue("created", now);
        params.addValue("updated", now);

        try {
            Integer id = insert.executeAndReturnKey(params).intValue();
            task.setId(id);
            return task;
        } catch (Exception e) {
            var message = "Error inserting task";
            log.error(message, e);
            throw new SaveTaskException(message);
        }
    }


    public Task findById(Integer id) {
        try {
            return template.query("select * from task where id = ?", ROW_MAPPER, id).getFirst();
        } catch (Exception e) {
            var message = "Task with id: " + id + " not found ";
            log.error(message, e);
            throw new TaskNotFoundException(message);
        }
    }


    public List<Task> findAll() {
        return template.query("select * from task", ROW_MAPPER);
    }


    public int update(Task task) {
        String sql = "UPDATE task SET title = ?, description = ?, status = ?::status_enum, updated = ? WHERE id = ?";
        return template.update(sql, task.getTitle(), task.getDescription(), task.getStatus().name(), LocalDateTime.now(), task.getId());
    }

    public int delete(Integer id) {
        return template.update("delete from task where id = ?", id);
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Task.builder()
                    .id(rs.getInt("id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .status(Task.Status.valueOf(rs.getString("status")))
                    .created(rs.getTimestamp("created").toLocalDateTime())
                    .updated(rs.getTimestamp("updated").toLocalDateTime())
                    .build();
        }
    }
}
