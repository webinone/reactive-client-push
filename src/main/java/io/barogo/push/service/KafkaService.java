package io.barogo.push.service;

import java.io.IOException;
import java.util.Collections;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

  private final ReactiveRedisOperations<String, String> reactiveRedisOperations;
  @Qualifier("kafkaSender")
  private final KafkaSender<String, Object> kafkaSender;
  @Qualifier("receiverStringOptions")
  private final ReceiverOptions<String, String> receiverOptions;

  private Flux<ReceiverRecord<String, String>> testTopicStream;

  @PostConstruct
  public void init() throws IOException {    // Consumer를 열어놓음

    log.info(">>>>>>>>>>> kafka topic 열림 !!!!");
    testTopicStream = createTopicCache(receiverOptions, "zoo.queueing.push.barogo.test.websocket");
  }

  public Flux<ReceiverRecord<String, String>> getTestTopicFlux() {
    return testTopicStream;
  }

  public Mono<Boolean> sendKafka(String key, Object value) {

    String topic = "zoo.queueing.push.barogo.test.websocket";

    return kafkaSender.createOutbound()
        .send(Mono.just(new ProducerRecord<>(topic, key, value)))  // 해당 topic으로 message 전송
        .then()
        .map(ret -> true)
        .onErrorResume(e -> {
          System.out.println("Kafka send error");
          return Mono.just(false);
        });
  }

  private <T, G> Flux<ReceiverRecord<T, G>> createTopicCache(ReceiverOptions<T, G> receiverOptions, String topicName) {
    ReceiverOptions<T, G> options = receiverOptions.subscription(Collections.singleton(topicName));
    log.info(">>>>>>>> 여기 걸리냐??????????");
    return KafkaReceiver.create(options).receive().cache();
  }
}
