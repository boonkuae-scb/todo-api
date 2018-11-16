package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.repository.TodoRepository;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAllByUserId(String userId) {
        return todoRepository.findAllByUserIdOrderByIsPinDescDateAsc(userId);
    }


    public Map<String, TextMessage> processPendingTodo() {
        List<Todo> pendingTodoList = todoRepository.findByDateLessThanAndIsSuccessIsFalseOrderByDateAsc(new Date());
        Map<String, TextMessage> lineMessages = new HashMap<>();
        BuildTextMessage(pendingTodoList, lineMessages, "Tasks to be done.\n\n");

        return lineMessages;
    }


    public Map<String, TextMessage> processSummaryTodo() {
        Date last24Hr = new Date(new Date().getTime() - 86400000);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<Todo> completedTodoList = todoRepository.findByUpdatedAtGreaterThanAndIsSuccessIsTrueOrderByDateAsc(last24Hr);
        Map<String, TextMessage> lineMessages = new HashMap<>();
        BuildTextMessage(completedTodoList, lineMessages, "Tasks completed .\n\n");

        return lineMessages;
    }

    private void BuildTextMessage(List<Todo> pendingComplete, Map<String, TextMessage> lineMessages, String headerMessage) {
        for (Todo todo : pendingComplete) {
            TextMessage message = lineMessages.get(todo.getUserId());
            if (message == null) {
                TextMessage textMessage = new TextMessage(headerMessage + todo.getTaskName() + "\n");
                lineMessages.put(todo.getUserId(), textMessage);

            } else {
                TextMessage newMessage = new TextMessage(message.getText() + todo.getTaskName() + "\n");
                lineMessages.put(todo.getUserId(), newMessage);
            }
        }
    }


    public Todo saveOrUpdateTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        Todo todoExist = todoRepository.findOneById(todo.getId());
        if (todoExist == null) {
            throw new TodoException("Todo doesn't exists");
        }

        if (todo.getIsSuccess() != todoExist.getIsSuccess() && todo.getIsSuccess()) {
            todo.setUpdatedAt(new Date());
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
                String timePattern1 = "\\d{2}:\\d{2}";
                String timePattern2 = "\\d{2}:\\d{1}";
                String timePattern3 = "\\d{1}:\\d{2}";
                if (!todoArray[2].matches(timePattern1) &&
                        !todoArray[2].matches(timePattern2) &&
                        !todoArray[2].matches(timePattern3)
                ) {
                    return "Input invalid time format( Type help for more detail )";
                }
                defaultTime = todoArray[2];
            }

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
                    String pattern2M2D = "\\d{2}/\\d{2}/\\d{2}";
                    String pattern1M1D = "\\d{1}/\\d{1}/\\d{2}";
                    String pattern2M1D = "\\d{2}/\\d{1}/\\d{2}";
                    String pattern1M2D = "\\d{1}/\\d{2}/\\d{2}";

                    if (!todoArray[1].matches(pattern2M2D) &&
                            !todoArray[1].matches(pattern1M1D) &&
                            !todoArray[1].matches(pattern2M1D) &&
                            !todoArray[1].matches(pattern1M2D)
                    ) {
                        return "Input invalid date format( Type help for more detail )";
                    }
            }
            date = formatter.parse(todoArray[1] + " " + defaultTime);


            Todo todo = new Todo();
            todo.setUserId(userId);
            todo.setTaskName(todoArray[0]);
            todo.setDate(date);
            todo.setIsPin(false);
            todo.setIsSuccess(false);
            Todo todoSaved = this.saveOrUpdateTodo(todo);
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
