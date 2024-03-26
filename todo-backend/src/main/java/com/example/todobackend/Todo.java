package com.example.todobackend;

public record Todo(Integer id, String description) {
    public Todo withId(Integer newId) {
        return new Todo(newId, description);
    }
}
