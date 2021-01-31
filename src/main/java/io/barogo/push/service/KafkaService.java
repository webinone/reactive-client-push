package io.barogo.push.service;

import java.io.IOException;
import java.util.Collections;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${kafka.topic.test.websocket}")
  private String testTopicName;

  @Qualifier("kafkaSender")
  private final KafkaSender<String, Object> kafkaSender;
  @Qualifier("receiverStringOptions")
  private final ReceiverOptions<String, String> receiverOptions;

  private Flux<ReceiverRecord<String, String>> testTopicStream;

  @PostConstruct
  public void init() throws IOException {    // Consumer를 열어놓음

    log.info(">>>>>>>>>>> kafka topic consume init !!!!");
    testTopicStream = createTopicCache(receiverOptions, testTopicName);
  }

  public Flux<ReceiverRecord<String, String>> getTestTopicFlux() {
    return testTopicStream;
  }

  public Mono<Boolean> sendKafka(String key, Object value) {
    return kafkaSender.createOutbound()
        .send(Mono.just(new ProducerRecord<>(testTopicName, key, value)))  // 해당 topic으로 message 전송
        .then()
        .map(ret -> true)
        .onErrorResume(e -> {
          System.out.println("Kafka send error");
          return Mono.just(false);
        });
  }

  private <T, G> Flux<ReceiverRecord<T, G>> createTopicCache(ReceiverOptions<T, G> receiverOptions, String topicName) {
    ReceiverOptions<T, G> options = receiverOptions.subscription(Collections.singleton(topicName));
    log.info(">>>>>>>> kafka topic consume topicName : {}", topicName);
    log.info(">>>>>>>> kafka topic consume groupID : {}", receiverOptions.groupId());
//    return KafkaReceiver.create(options).receive();
    return KafkaReceiver.create(options).receive().cache();
  }
}
