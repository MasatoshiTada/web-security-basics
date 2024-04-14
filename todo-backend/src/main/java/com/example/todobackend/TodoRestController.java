package com.example.todobackend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoRestController {

    private final TodoService todoService;

    public TodoRestController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<TodoResponse> listAll() {
        return todoService.listAll()
                .stream()
                .map(todo -> new TodoResponse(todo))
                .toList();
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody TodoRequest todoRequest) {
        Todo todo = todoRequest.toEntity();
        Todo newTodo = todoService.add(todo);
        URI location = URI.create("/api/todos/" + newTodo.id());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> done(@PathVariable Integer id) {
        todoService.done(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
