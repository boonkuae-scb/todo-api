package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.repository.TodoRepository;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        int[] dateTimeInput = new int[5]; // {dd,MM,yyyy,H,i}
        dateTimeInput[3] = 12;
        dateTimeInput[4] =0;
        String[] todoInputArray = todoContent.split(" : ");
        try {
            if (todoInputArray.length < 2 || todoInputArray.length > 3) {
                return "Input wrong format( Type help for more detail )";
            }

            if (todoInputArray[0].isEmpty()) {
                return "Input task name cloud not be empty( Type help for more detail )";
            }

            if (todoInputArray.length == 3) {
                String timePattern1 = "\\d{2}:\\d{2}";
                String timePattern2 = "\\d{2}:\\d{1}";
                String timePattern3 = "\\d{1}:\\d{2}";
                String timePattern4 = "\\d{1}:\\d{1}";
                if (!todoInputArray[2].matches(timePattern1) &&
                        !todoInputArray[2].matches(timePattern2) &&
                        !todoInputArray[2].matches(timePattern3) &&
                        !todoInputArray[2].matches(timePattern4)
                ) {
                    return "Input invalid time format( Type help for more detail )";
                }
                String[] newTime = todoInputArray[2].split(":");
                dateTimeInput[3] = Integer.parseInt(newTime[0].trim());
                dateTimeInput[4] = Integer.parseInt(newTime[1].trim());
            }

            Calendar cal = Calendar.getInstance();
            switch (todoInputArray[1].trim().toLowerCase()) {
                case "today":

                    Date today = new Date();
                    cal.setTime(today);
                    break;
                case "tomorrow":
                    Date tomorrow = new Date(new Date().getTime() + 86400000);
                    cal.setTime(tomorrow);

                    ;
                    break;
                default:
                    String pattern2M2D = "\\d{2}/\\d{2}/\\d{2}";
                    String pattern1M1D = "\\d{1}/\\d{1}/\\d{2}";
                    String pattern2M1D = "\\d{2}/\\d{1}/\\d{2}";
                    String pattern1M2D = "\\d{1}/\\d{2}/\\d{2}";

                    if (!todoInputArray[1].matches(pattern2M2D) &&
                            !todoInputArray[1].matches(pattern1M1D) &&
                            !todoInputArray[1].matches(pattern2M1D) &&
                            !todoInputArray[1].matches(pattern1M2D)
                    ) {
                        return "Input invalid date format( Type help for more detail )";
                    }

                    String pattern = "dd/MM/yy";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    Date dateInput = format.parse(todoInputArray[1].trim());
                    cal.setTime(dateInput);

                    break;
            }

            dateTimeInput[0] = cal.get(Calendar.DATE);
            dateTimeInput[1] = cal.get(Calendar.MONTH);
            dateTimeInput[2] = cal.get(Calendar.YEAR);

            Calendar calendar = new GregorianCalendar(
                    dateTimeInput[2],
                    dateTimeInput[1],
                    dateTimeInput[0],
                    dateTimeInput[3],
                    dateTimeInput[4]
            );

            Date todoDate = calendar.getTime();

            Todo todo = new Todo();
            todo.setUserId(userId);
            todo.setTaskName(todoInputArray[0]);
            todo.setDate(todoDate);
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
