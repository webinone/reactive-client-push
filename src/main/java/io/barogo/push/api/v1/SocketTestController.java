package io.barogo.push.api.v1;

import io.barogo.push.service.EchoService;
import io.barogo.push.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/api/v1"})
@RequiredArgsConstructor
public class SocketTestController {

  private final ReactiveRedisOperations<String, String> reactiveRedisOperations;
  private final EchoService echoService;
  private final KafkaService kafkaService;

  @GetMapping("/redis-test")
  public Flux<Object> all() {
    return reactiveRedisOperations.keys("*")
        .flatMap(reactiveRedisOperations.opsForValue()::get);
  }

  @PostMapping("/redis-test")
  public Mono<String> postRedis() {
    return echoService.saveRedis("test");
  }

  @PostMapping("/kafka-test")
  public Mono<Boolean> postKafka() {
    return kafkaService.sendKafka("key", "kafka controller value");
  }

}
