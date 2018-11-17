package com.bk.todos.services;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.repository.TodoRepository;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void todoProcessor_Fail_When__WrongFormat() {
        String actual = todoService.todoProcessor("1", "Test");
        Assert.assertEquals("Input wrong format( Type help for more detail )", actual);
    }

    @Test
    public void todoProcessor_Fail_When__InputWrongTaskName() {
        String actual = todoService.todoProcessor("1", " : 22/11/18 : 10:00");
        Assert.assertEquals("Input task name cloud not be empty( Type help for more detail )", actual);
    }


    @Test
    public void todoProcessor_Fail_When_WrongTime() {
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18 : 10121:0012");
        Assert.assertEquals("Input invalid time format( Type help for more detail )", actual);
    }

    @Test
    public void todoProcessor_Success_When__InputToday() throws ParseException {
        // Given
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setDate(cal.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Task Name : today");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        // Verify
        verify(todoRepository, times(1)).save(any(Todo.class));

    }


    @Test
    public void todoProcessor_Success_When__InputTomorrow() throws ParseException {
        // Given
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setDate(calendar.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Task Name : tomorrow");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        // Verify
        verify(todoRepository, times(1)).save(any(Todo.class));

    }


    @Test
    public void todoProcessor_Success_When__InputFullDateTime() {
        // Given
        Calendar calendar = new GregorianCalendar(
                2018,
                5,
                3,
                12,
                0,
                0
        );
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        mockTodo1.setDate(calendar.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        //Buy milk : 3/5/18 : 13:00
        String actual = todoService.todoProcessor("1", "Buy milk : 3/5/18 : 13:00");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        // Verify
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessor_Success_When__Input2DigitDate() {

        // Given
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");
        Calendar calendar = Calendar.getInstance();

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        mockTodo1.setDate(calendar.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Buy milk : 03/05/18 : 13:00");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        // Verify
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessor_Success_When_Input1M2D() {

        // Given
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");
        Calendar calendar = Calendar.getInstance();

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        mockTodo1.setDate(calendar.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Buy milk : 3/13/18 : 13:00");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        // Verify
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessor_Success_When_Input2M1D() {

        // Given
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");
        Calendar calendar = Calendar.getInstance();

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        mockTodo1.setDate(calendar.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Buy milk : 3/10/18 : 13:00");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        // Verify
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessor_Success_When_NotInputTime() throws ParseException {
        // Given
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");
        Calendar calendar = Calendar.getInstance();

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        mockTodo1.setDate(calendar.getTime());

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18");
        Assert.assertEquals("Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }

    @Test
    public void todoProcessor_Success_When_InputTime() throws ParseException {
        // Given
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMMM YYYY hh:mm a");
        Calendar calendar = Calendar.getInstance();

        Todo mockTodo1 = new Todo();
        mockTodo1.setId("TD1");
        mockTodo1.setTaskName("Buy milk");
        mockTodo1.setDate(calendar.getTime());
        String expect = "Create new Todo - " + mockTodo1.getTaskName() + " : " + ft.format(mockTodo1.getDate()) + " successful";

        // When
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo1);

        // Then
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18 : 13:56");
        Assert.assertEquals(expect, actual);

        verify(todoRepository, times(1)).save(any(Todo.class));

    }

    @Test
    public void todoProcessor_Fail_WhenCannotSave() {
        // Given
        Mockito.when(todoRepository.save(any(Todo.class))).thenReturn(null);

        // When
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/18 : 10:00");

        // Then
        Assert.assertEquals("Cannot save", actual);

        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    public void todoProcessor_Fail_When_WrongDateFormat() {
        String actual = todoService.todoProcessor("1", "Task Name : 22/11/2018 : 10:00");
        Assert.assertEquals("Input invalid date format( Type help for more detail )", actual);
    }


    @Test
    public void findAllByUserId_Success_When_TodoExist() {
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
    public void processPending_Success_HappyPath() {
        Calendar calYesterday = Calendar.getInstance();
        calYesterday.add(Calendar.DATE, -1);


        Todo mockTodo1 = new Todo();
        mockTodo1.setTaskName("TD1");
        mockTodo1.setUserId("TD1");
        mockTodo1.setIsSuccess(false);
        mockTodo1.setDate(calYesterday.getTime());


        Todo mockTodo2 = new Todo();
        mockTodo2.setTaskName("TD2");
        mockTodo2.setUserId("TD2");
        mockTodo2.setIsSuccess(false);
        mockTodo2.setDate(calYesterday.getTime());


        Todo mockTodo3 = new Todo();
        mockTodo3.setTaskName("TD3");
        mockTodo3.setUserId("TD2");
        mockTodo3.setIsSuccess(false);
        mockTodo3.setDate(calYesterday.getTime());


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
    public void processSummary_Success_When_HappyPath() {
        Calendar calToday = Calendar.getInstance();
        Calendar calYesterday = Calendar.getInstance();
        calYesterday.add(Calendar.DATE, -1);


        Todo mockTodo1 = new Todo();
        mockTodo1.setTaskName("TD1");
        mockTodo1.setUserId("TD1");
        mockTodo1.setIsSuccess(true);
        mockTodo1.setDate(calYesterday.getTime());
        mockTodo1.setUpdatedAt(calToday.getTime());


        Todo mockTodo2 = new Todo();
        mockTodo2.setTaskName("TD2");
        mockTodo2.setUserId("TD2");
        mockTodo2.setIsSuccess(true);
        mockTodo2.setDate(calYesterday.getTime());
        mockTodo2.setUpdatedAt(calToday.getTime());


        Todo mockTodo3 = new Todo();
        mockTodo3.setTaskName("TD3");
        mockTodo3.setUserId("TD2");
        mockTodo3.setIsSuccess(true);
        mockTodo3.setDate(calYesterday.getTime());
        mockTodo3.setUpdatedAt(calToday.getTime());


        List<Todo> mockTodoList = new ArrayList<>();
        mockTodoList.add(mockTodo1);
        mockTodoList.add(mockTodo2);
        mockTodoList.add(mockTodo3);

        // When
        when(todoRepository.findByUpdatedAtGreaterThanAndIsSuccessIsTrueOrderByDateAsc(any())).thenReturn(mockTodoList);

        System.out.println("mockTodoList = " + mockTodoList);
        // Then
        Map<String, TextMessage> actual = todoService.processSummaryTodo();
        Assert.assertEquals(2, actual.size());

        // verify
        verify(todoRepository, times(1)).findByUpdatedAtGreaterThanAndIsSuccessIsTrueOrderByDateAsc(any());
    }

    @Test
    public void updateTodo_Success_When_HappyPath() {
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
    public void updateTodo_Fail_When_Record_NotExist() {
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
