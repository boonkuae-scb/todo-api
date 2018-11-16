package com.bk.todos.controller;

import com.bk.todos.entity.Todo;
import com.bk.todos.services.MapValidationErrorServices;
import com.bk.todos.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/todo")
@CrossOrigin("*")
public class TodoController {

    private final TodoService todoService;
    private final MapValidationErrorServices mapValidationErrorServices;


    @Autowired
    public TodoController(TodoService todoService, MapValidationErrorServices mapValidationErrorServices) {
        this.todoService = todoService;
        this.mapValidationErrorServices = mapValidationErrorServices;

    }

    @GetMapping("/user/{userId}")
    public List<Todo> getAllTodo(@PathVariable String userId) {
        return todoService.findAllByUserId(userId);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<?> updateTodo(@Valid @RequestBody Todo todo, BindingResult result) {
        ResponseEntity<?> errorMap = mapValidationErrorServices.MapValidationService(result);
        if (errorMap != null) return errorMap;

        return new ResponseEntity<>(todoService.updateTodo(todo), HttpStatus.OK);
    }
}
