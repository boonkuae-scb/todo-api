package com.bk.todos.controller;

import com.bk.todos.model.AuthRequest;
import com.bk.todos.model.Response;
import com.bk.todos.model.ResponseModel;
import com.bk.todos.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping()
    public HttpEntity<ResponseModel> getToken(@RequestBody AuthRequest request
    ) {
        return new ResponseModel(Response.SUCCESS.getContent(), authService.getAccessToken(request)).build(HttpStatus.OK);
    }
}