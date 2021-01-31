package io.barogo.push.service;

import io.barogo.push.model.entity.TodoEntity;
import io.barogo.push.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class TodoService {
  private final TodoRepository todoRepository;

  public Flux<TodoEntity> readWorkerLocations() {
    return this.todoRepository.findAll();
  }
}
