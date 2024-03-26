package com.example.todobackend;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true)
    public List<Todo> listAll() {
        return todoRepository.listAll();
    }

    @Transactional(readOnly = false)
    public Todo add(Todo todo) {
        return todoRepository.add(todo);
    }
}
