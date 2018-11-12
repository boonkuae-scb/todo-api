package com.bk.todos.repository;

import com.bk.todos.entity.Todo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface TodoRepository  extends MongoRepository<Todo, String> {

    List<Todo> findAllByUserIdOrderByIsSuccessAscIsPinDescDateAsc(String userId);
}
