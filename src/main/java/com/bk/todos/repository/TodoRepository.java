package com.bk.todos.repository;

import com.bk.todos.entity.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

    Todo findOneById(String Id);

    List<Todo> findByDateLessThanAndIsSuccessIsFalseOrderByDateAsc(Date date);

    List<Todo> findByUpdatedAtGreaterThanAndIsSuccessIsTrueOrderByDateAsc(Date date);

    List<Todo> findAllByUserIdOrderByIsPinDescDateAsc(String userId);
}
