package io.barogo.push.api.v1;

import io.barogo.push.model.entity.WorkerLocation;
import io.barogo.push.service.EchoService;
import io.barogo.push.service.KafkaService;
import io.barogo.push.service.WorkerLocationService;
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
  private final WorkerLocationService workerLocationService;
  private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
  private final ChannelTopic topic;

  @GetMapping("/redis-test")
  public Flux<Object> redisAll() {
    return reactiveRedisOperations.keys("*")
        .flatMap(reactiveRedisOperations.opsForValue()::get);
  }

  @PostMapping("/redis-test")
  public Mono<String> postRedis() {
    return echoService.saveRedis("test");
  }

  @PostMapping("/redis-pub/{variety}")
  public Mono<Long> postRedisPub(@PathVariable String variety) {
    log.info("New Coffee with variety '" + variety + "' send to Channel '" + topic.getTopic() + "'.");
    return reactiveRedisTemplate.convertAndSend(topic.getTopic(), variety);
  }

  @PostMapping("/kafka-test")
  public Mono<Boolean> postKafka() {
    return kafkaService.sendKafka("key", "kafka controller value");
  }

  @GetMapping("/r2dbc-test")
  public Flux<WorkerLocation> r2dbcAll(
  ) {
    return workerLocationService.readWorkerLocations();
  }

}
