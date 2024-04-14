package com.example.todobackend;

public record LoginResult(String sessionId, String csrfToken) {
}
