package io.barogo.push.repository;

import io.barogo.push.model.entity.TodoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends ReactiveCrudRepository<TodoEntity, Long> {
}
