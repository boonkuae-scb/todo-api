package com.bk.todos.controller;


import com.bk.todos.model.AuthRequest;
import com.bk.todos.model.AuthResponse;
import com.bk.todos.services.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTokenHappyPath() throws Exception {
        AuthRequest mockAuthRequest = new AuthRequest();
        mockAuthRequest.setCode("000010");
        String jsonPost = "{\"code\": \""+mockAuthRequest.getCode()+"\"}";

        AuthResponse mockAuthResponse = new AuthResponse();
        mockAuthResponse.setScope("test");
        mockAuthResponse.setAccessToken("my token");

        when(authService.callRequestAccessToken(any(AuthRequest.class)))
                .thenReturn(mockAuthResponse);

        mockMvc
                .perform(
                        post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPost)
                )
                .andExpect(status().isOk()).andReturn();

        verify(authService, times(1)).callRequestAccessToken(any(AuthRequest.class));
    }
}