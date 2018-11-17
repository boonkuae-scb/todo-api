package com.bk.todos.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "todo")
@Data
public class Todo {
    @Id
    private String id;

    private String userId;

    private String taskName;

    private Date date;

    private Date updatedAt;

    private Boolean isPin;
    private Boolean isSuccess;

}