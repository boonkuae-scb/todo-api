package com.bk.todos.controller;

import com.bk.todos.entity.Todo;
import com.bk.todos.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@CrossOrigin
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/{userId}")
    public List<Todo> getAllTodo(@PathVariable String userId) {
        return todoService.findAllByUserId(userId);
    }

    @PutMapping("/{userId}/{id}")
    public List<Todo> updateTodo(@PathVariable String userId, @PathVariable String id) {
        return todoService.findAllByUserId(userId);
    }

}
