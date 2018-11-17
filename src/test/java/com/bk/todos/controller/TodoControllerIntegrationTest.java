package com.bk.todos.controller;

import com.bk.todos.entity.Todo;
import com.bk.todos.exceptions.TodoException;
import com.bk.todos.services.TodoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoControllerIntegrationTest {

    @Autowired
    TodoController todoController;

    @Autowired
    TodoService todoService;

    @Before
    public void setUp() throws Exception {
        // Clear

        // Insert Todo
        Todo iTodo = new Todo();
        iTodo.setId("todoIdSuccess");
        iTodo.setTaskName("Task Name Success");
        iTodo.setUserId("unitTestUserIdSuccess");

        Todo newTodo = todoService.saveOrUpdateTodo(iTodo);
    }


    @Test
    public void updateTodoFail() {
        Todo aTodo = new Todo();
        aTodo.setId("todoIdFail");
        aTodo.setTaskName("Task Name Fail");
        aTodo.setUserId("unitTestUserIdFail");

        try {
            BindingResult result = mock(BindingResult.class);
            ResponseEntity<?> actual = todoController.updateTodo(aTodo, result);
        } catch (TodoException todoException) {
            assertEquals("Todo doesn't exists", todoException.getMessage());
        }
    }

    @Test
    public void getAllTodo() {


        Todo aTodo = new Todo();
        aTodo.setId("todoIdSuccess");
        aTodo.setTaskName("Task Name Success");
        aTodo.setUserId("unitTestUserIdSuccess");
        List<Todo> totoList = Arrays.asList(aTodo);

        List<Todo> actual = todoController.getAllTodo(aTodo.getUserId());

        assertEquals(totoList, actual);


    }

}