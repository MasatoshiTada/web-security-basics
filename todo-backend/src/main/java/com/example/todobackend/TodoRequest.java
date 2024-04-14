package com.example.todobackend;

public record TodoRequest(String description) {

    public Todo toEntity() {
        return new Todo(null, description, null);
    }
}
