package io.barogo.push.api.v1;

import io.barogo.push.model.entity.TodoEntity;
import io.barogo.push.service.EchoService;
import io.barogo.push.service.KafkaService;
import io.barogo.push.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping({"/api/v1/demo"})
@RequiredArgsConstructor
public class SocketDemoController {

  private final ReactiveRedisOperations<String, String> reactiveRedisOperations;
  private final EchoService echoService;
  private final KafkaService kafkaService;
  private final TodoService todoService;
  private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
  private final ChannelTopic topic;

  @PostMapping("/redis-save/{message}")
  public Mono<String> postRedis(@PathVariable String message) {
    return echoService.saveRedis("test");
  }

  @PostMapping("/redis-pub/{message}")
  public Mono<Long> postRedisPub(@PathVariable String message) {
    log.info("New Coffee with variety '" + message + "' send to Channel '" + topic.getTopic() + "'.");
    return reactiveRedisTemplate.convertAndSend(topic.getTopic(), message);
  }

  @PostMapping("/kafka-pub/{message}")
  public Mono<Boolean> postKafka(@PathVariable String message) {
    return kafkaService.sendKafka("key", message);
  }

  @GetMapping("/r2dbc-all")
  public Flux<TodoEntity> r2dbcAll(
  ) {
    return todoService.readWorkerLocations();
  }

}
