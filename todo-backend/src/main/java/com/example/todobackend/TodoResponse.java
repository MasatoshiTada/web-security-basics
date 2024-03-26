package com.example.todobackend;

public record TodoResponse(Integer id, String description) {

    public TodoResponse(Todo todo) {
        this(todo.id(), todo.description());
    }
}
