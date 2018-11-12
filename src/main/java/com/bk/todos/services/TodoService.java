package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAllByUserId(String userId) {
        return todoRepository.findAllByUserIdOrderByIsSuccessAscIsPinDescDateAsc(userId);
    }

    public Todo saveOrUpdateTodo(Todo todo) {
        try {
            return todoRepository.save(todo);
        } catch (Exception e) {
            throw new TodoException("Todo Code '" + todo.getId() + "' already exists");
        }
    }

    public Todo updateTodo(Todo todo)
    {
        Optional<Todo> todoOptional = todoRepository.findById(todo.getId());
        if (!todoOptional.isPresent())
        {
            throw new TodoException("Todo doesn't exists");
        }
        return this.saveOrUpdateTodo(todo);
    }

}
