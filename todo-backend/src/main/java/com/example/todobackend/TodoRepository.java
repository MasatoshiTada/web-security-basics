package com.example.todobackend;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoRepository {

    private final JdbcClient jdbcClient;

    public TodoRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Todo> listAll() {
        return jdbcClient.sql("SELECT id, description FROM todo ORDER BY id")
                .query(new DataClassRowMapper<>(Todo.class))
                .list();
    }

    public Todo add(Todo todo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO todo (description) VALUES (:description)")
                .param("description", todo.description())
                .update(keyHolder, "id");
        int newId = keyHolder.getKey().intValue();
        return todo.withId(newId);
    }
}
