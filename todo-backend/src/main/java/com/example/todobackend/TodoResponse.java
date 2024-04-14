package com.example.todobackend;

public record TodoResponse(Integer id, String description, Boolean done) {

    public TodoResponse(Todo todo) {
        this(todo.id(), todo.description(), todo.done());
    }
}
