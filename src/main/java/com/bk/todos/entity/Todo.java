package com.bk.todos.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Document
@Data
public class Todo {
    @Id
    private String id;

    @NotBlank(message = "Project name is required")
    private String userId;

    private String taskName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    private Date date;

    private Boolean isPin;
    private Boolean isSuccess;

}