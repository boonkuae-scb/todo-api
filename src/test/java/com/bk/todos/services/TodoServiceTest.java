package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.repository.TodoRepository;
import com.linecorp.bot.model.message.TextMessage;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TodoServiceTest {

    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Before
    public void setup() {
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
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

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
        Date date = formatter.parse(tommorrow.format(tomorrow) + " 12:00");
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : tomorrow");


        // Then
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }


    @Test
    public void todoProcessorSuccessWhenInputFullDateTime() {

        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");
        Date date = new Date(2018, 5, 3, 12, 0, 0);
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);


        // When
        //Buy milk : 3/5/18 : 13:00
        String actual = todoService.todoProcessor("1", "Buy milk : 3/5/18 : 13:00");

        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);


        // Then
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessorSuccessWhenInput2Digit() {

        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");
        Date date = new Date(2018, 5, 3, 12, 0, 0);
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);


        // When
        //Buy milk : 03/05/18 : 13:00
        String actual = todoService.todoProcessor("1", "Buy milk : 03/05/18 : 13:00");

        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);


        // Then
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessorSuccessWhenInput1M2D() {

        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");
        Date date = new Date(2018, 5, 3, 12, 0, 0);
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);


        // When
        //Buy milk : 03/05/18 : 13:00
        String actual = todoService.todoProcessor("1", "Buy milk : 3/13/18 : 13:00");

        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);


        // Then
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessorSuccessWhenInput2M1D() {

        // Given
        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yy hh:mm");
        Date date = new Date(2018, 5, 3, 12, 0, 0);
        mockTodo1.setDate(date);
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);


        // When
        //Buy milk : 03/05/18 : 13:00
        String actual = todoService.todoProcessor("1", "Buy milk : 3/10/18 : 13:00");

        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

        // Then
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

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
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful", actual);

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
        String expect = "Create new Todo - " + mockTodo1.getTaskName() + " : " + mockTodo1.getDate() + " successful";
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

        // When
        when(todoRepository.findAllByUserIdOrderByIsPinDescDateAsc(anyString())).thenReturn(todoList);

        // Then
        List<Todo> actual = todoService.findAllByUserId("userId");

        Assert.assertEquals(2, actual.size());
        Assert.assertEquals(mockTodo1.getId(), actual.get(0).getId());
        Assert.assertEquals(mockTodo2.getId(), actual.get(1).getId());

        // verify
        verify(todoRepository, times(1)).findAllByUserIdOrderByIsPinDescDateAsc(anyString());
    }

    @Test
    public void processPendingTodoHappyPath() {
        Todo mockTodo1 = new Todo();
        mockTodo1.setTaskName("TD1");
        mockTodo1.setUserId("TD1");
        mockTodo1.setIsPin(false);

        Todo mockTodo2 = new Todo();
        mockTodo2.setTaskName("TD2");
        mockTodo2.setUserId("TD2");
        mockTodo2.setIsPin(false);

        Todo mockTodo3 = new Todo();
        mockTodo3.setTaskName("TD3");
        mockTodo3.setUserId("TD2");
        mockTodo3.setIsPin(false);


        List<Todo> mockTodoList = new ArrayList<>();
        mockTodoList.add(mockTodo1);
        mockTodoList.add(mockTodo2);
        mockTodoList.add(mockTodo3);

        // When
        when(todoRepository.findByDateLessThanAndIsSuccessIsFalseOrderByDateAsc(any())).thenReturn(mockTodoList);
        Map<String, TextMessage> actual = todoService.processPendingTodo();

        // Then
        Assert.assertEquals(2, actual.size());

        // verify
        verify(todoRepository, times(1)).findByDateLessThanAndIsSuccessIsFalseOrderByDateAsc(any());
    }


    @Test
    public void processSummaryHappyPath() {
        Todo mockTodo1 = new Todo();
        mockTodo1.setTaskName("TD1");
        mockTodo1.setUserId("TD1");
        mockTodo1.setIsSuccess(true);

        Todo mockTodo2 = new Todo();
        mockTodo2.setTaskName("TD2");
        mockTodo2.setUserId("TD2");
        mockTodo2.setIsSuccess(true);

        Todo mockTodo3 = new Todo();
        mockTodo3.setTaskName("TD3");
        mockTodo3.setUserId("TD2");
        mockTodo3.setIsSuccess(true);


        List<Todo> mockTodoList = new ArrayList<>();
        mockTodoList.add(mockTodo1);
        mockTodoList.add(mockTodo2);
        mockTodoList.add(mockTodo3);

        // When
        when(todoRepository.findByUpdatedAtGreaterThanAndIsSuccessIsTrueOrderByDateAsc(any())).thenReturn(mockTodoList);
        Map<String, TextMessage> actual = todoService.processSummaryTodo();

        // Then
        Assert.assertEquals(2, actual.size());

        // verify
        verify(todoRepository, times(1)).findByUpdatedAtGreaterThanAndIsSuccessIsTrueOrderByDateAsc(any());
    }

    @Test
    public void updateTodoHappyPath() {
        // Given
        Todo todoExist = new Todo();
        todoExist.setId("TD1");
        todoExist.setTaskName("Task name1");
        todoExist.setUpdatedAt(null);
        todoExist.setIsSuccess(false);

        Todo todoWillUpdate = new Todo();
        todoWillUpdate.setId("TD1");
        todoWillUpdate.setTaskName("Task name1");
        todoWillUpdate.setUpdatedAt(null);
        todoWillUpdate.setIsSuccess(true);


        Todo todoUpdated = new Todo();
        todoUpdated.setId("TD1");
        todoUpdated.setTaskName("Task name1");
        todoUpdated.setUpdatedAt(new Date());
        todoUpdated.setIsSuccess(true);


        // when
        when(todoRepository.findOneById(anyString())).thenReturn(todoExist);
        when(todoRepository.save(any())).thenReturn(todoUpdated);

        // Then
        Todo actual = todoService.updateTodo(todoWillUpdate);

        Assert.assertEquals(todoExist.getId(), actual.getId());
        Assert.assertNotNull(actual.getUpdatedAt());

        // Verify
        verify(todoRepository, times(1)).findOneById(any());
        verify(todoRepository, times(1)).save(any());
    }


    @Test
    public void updateTodoFailWhenNotExitst() {
        // Given
        Todo todoWillUpdate = new Todo();
        todoWillUpdate.setId("TD99");

        // when
        when(todoRepository.findOneById(anyString())).thenReturn(null);


        // Then
        try {
            Todo todo = todoService.updateTodo(todoWillUpdate);
        } catch (TodoException e) {
            Assert.assertTrue(e instanceof TodoException);

        }
        // Verify
        verify(todoRepository, times(1)).findOneById(any());
    }
}
