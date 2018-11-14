package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.repository.TodoRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TodoServiceTest {

    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Before
    public void tearUp() {
        MockitoAnnotations.initMocks(this);
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void todoProcessorFailWhenWrongFormat() {
        String actual = todoService.todoProcessor("1", "Test");
        Assert.assertEquals("Input wrong format( Type help for more detail )", actual);
    }

    @Test
    public void todoProcessorFailWhenWrongTaskName() {
        String actual = todoService.todoProcessor("1", " : 22/11/18 : 10:00");
        Assert.assertEquals("Input task name cloud not be empty( Type help for more detail )", actual);
    }


    @Test
    public void todoProcessorFailWhenWrongTime() {
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18 : 10121:0012");
        Assert.assertEquals("Input invalid time format( Type help for more detail )", actual);
    }

    @Test
    public void todoProcessorSuccessWhenNotInputToday() throws ParseException {
        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");

        SimpleDateFormat today = new SimpleDateFormat("dd/M/yy 12:00:59");
        Date date = formatter.parse(today.format(new Date()));
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : today");


        // Then
        Assert.assertEquals("Create new Todo - "+ mockTodo1.getTaskName()+" : "+ mockTodo1.getDate()+" successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }


    @Test
    public void todoProcessorSuccessWhenNotInputTomorrow() throws ParseException {
        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");

        SimpleDateFormat tommorrow = new SimpleDateFormat("dd/M/yy");
        Date tomorrow = new Date(new Date().getTime() + 86400000);
        Date date = formatter.parse(tommorrow.format(tomorrow)+" 12:00");
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : tomorrow");


        // Then
        Assert.assertEquals("Create new Todo - "+ mockTodo1.getTaskName()+" : "+ mockTodo1.getDate()+" successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }


    @Test
    public void todoProcessorSuccessWhenNotInputTime() throws ParseException {
        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        String dateInString = "22/11/18";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy");
        Date date = formatter.parse(dateInString);
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18");


        // Then
        Assert.assertEquals("Create new Todo - "+ mockTodo1.getTaskName()+" : "+ mockTodo1.getDate()+" successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }

    @Test
    public void todoProcessorSuccessWhenInputTime() throws ParseException {
        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setId("Task Name");
        String dateInString = "22/11/18 13:45";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");
        Date date = formatter.parse(dateInString);
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18 : 13:56");


        // Then
        String expect = "Create new Todo - "+ mockTodo1.getTaskName()+" : "+ mockTodo1.getDate()+" successful";
        Assert.assertEquals(expect, actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }

    @Test
    public void todoProcessorFailWhenCannotSave() {
        // Given
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(null);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18 : 10:00");

        // Then
        Assert.assertEquals("Cannot save", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessorFailWhenWrongDateFormat() {
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/2018 : 10:00");
        Assert.assertEquals("Input invalid date format( Type help for more detail )", actual);
    }


    @Test
    public void findAllByUserIdSuccessWhenTodoExist() {
        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");

        Todo mockTodo2 = new Todo();
        mockTodo2.setId("TD2");

        List<Todo> todoList = new ArrayList<>();
        todoList.add(mockTodo1);
        todoList.add(mockTodo2);

        when(todoRepository.findAllByUserIdOrderByIsSuccessAscIsPinDescDateAsc(anyString())).thenReturn(todoList);

        // When
        List<Todo> actual = todoService.findAllByUserId("userId");

        // Then
        Assert.assertEquals(2, actual.size());
        Assert.assertEquals(mockTodo1.getId(), actual.get(0).getId());
        Assert.assertEquals(mockTodo2.getId(), actual.get(1).getId());

        verify(todoRepository, times(1)).findAllByUserIdOrderByIsSuccessAscIsPinDescDateAsc(anyString());
    }
}
