package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
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
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        Optional<Todo> todoOptional = todoRepository.findById(todo.getId());
        if (!todoOptional.isPresent()) {
            throw new TodoException("Todo doesn't exists");
        }
        return this.saveOrUpdateTodo(todo);
    }

    public String todoProcessor(String userId, String todoContent) {
        String defaultTime = "12:00";
        Date date = null;
        String[] todoArray = todoContent.split(" : ");
        try {
            if (todoArray.length < 2 || todoArray.length > 3) {
                return "Input wrong format( Type help for more detail )";
            }

            if (todoArray[0].isEmpty()) {
                return "Input task name cloud not be empty( Type help for more detail )";
            }


            if (todoArray.length == 3) {
                String timePattern = "\\d{2}:\\d{2}";
                if (!todoArray[2].matches(timePattern)) {
                    return "Input invalid time format( Type help for more detail )";
                }
                defaultTime = todoArray[2];
            }


            String datePattern1 = "\\d{2}/\\d{2}/\\d{2}";
            String datePattern2 = "\\d{2}/\\d{2}/\\d{2}";
            if (!todoArray[1].matches(datePattern1) || !todoArray[1].matches(datePattern2)) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");
                switch (todoArray[1].trim().toLowerCase()) {
                    case "today":
                        SimpleDateFormat today = new SimpleDateFormat("dd/M/yy");
                        date = formatter.parse(today.format(new Date()) + " " + defaultTime);
                        break;
                    case "tomorrow":
                        SimpleDateFormat tomorrowFormat = new SimpleDateFormat("dd/M/yy");
                        Date tomorrow = new Date(new Date().getTime() + 86400000);
                        date = formatter.parse(tomorrowFormat.format(tomorrow) + " " + defaultTime);
                        break;
                    default:
                        return "Input invalid date format( Type help for more detail )";
                }
            }


            Todo todo = new Todo();
            todo.setUserId(userId);
            todo.setTaskName(todoArray[0]);
            todo.setDate(date);
            todo.setIsPin(false);
            todo.setIsSuccess(false);

            Todo todoSaved = todoRepository.save(todo);
            if (todoSaved == null) {
                throw new ParseException("Cannot save", 0);
            }
            return "Create new Todo - " + todoSaved.getTaskName() + " : " + todoSaved.getDate() + " successful";
        } catch (ParseException e) {
            System.out.println("e.getMessage() = [" + e.getMessage() + "]");
            return "Cannot save";
        }
    }
}
