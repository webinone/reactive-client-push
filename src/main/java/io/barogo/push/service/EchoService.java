package io.barogo.push.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.barogo.push.model.dto.EchoMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import reactor.kafka.sender.KafkaSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class EchoService {

  private final ReactiveRedisOperations<String, String> reactiveRedisOperations;
//  @Qualifier("kafkaSender")
//  private final KafkaSender<String, Object> kafkaSender;
//  @Qualifier("receiverStringOptions")
//  private final ReceiverOptions<String, String> receiverOptions;
////  private final Sinks.Many<Object> sinksMany;
//  private Disposable disposable;
//  private Flux<ReceiverRecord<String, String>> testTopicStream;



  private ObjectMapper jsonMapper = new ObjectMapper();

//  @PostConstruct
//  public void init() throws IOException {    // Consumer를 열어놓음
//
//    log.info(">>>>>>>>>>> kafka topic 열림 !!!!");
//    testTopicStream = createTopicCache(receiverOptions, "zoo.queueing.push.barogo.test.websocket");
//  }
//
//  @PreDestroy
//  public void destroy() {
//    if (disposable != null) {
//      disposable.dispose();
//    }
//    kafkaSender.close();
//  }



  public String greeting(String message)  {

    // business logic 처리...

    log.info(">>>> EchoService gretting : " + message);
//    return Try.of(() -> {
//  tr          Thread.sleep(1000); //simulate delay

    EchoMessage echoMessage = null;
    String returnMessage = null;
    try {

      String messageId = UUID.randomUUID().toString();

      echoMessage = EchoMessage.builder()
          .id(messageId)
          .body(message)
          .sendAt(LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()).build();
      returnMessage = jsonMapper.writeValueAsString(echoMessage);

    }catch (Exception e) {
      log.error("Could not parse JSON object", e.toString());
    }

    return  returnMessage;
  }

  public Mono<String> saveRedis(String message) {
    String messageId = UUID.randomUUID().toString();
    return reactiveRedisOperations.opsForValue().set(messageId, message).map(String::valueOf);

  }

//  public Mono<Boolean> sendKafka(String key, Object value) {
//
//    String topic = "zoo.queueing.push.barogo.test.websocket";
//
//    return kafkaSender.createOutbound()
//        .send(Mono.just(new ProducerRecord<>(topic, key, value)))  // 해당 topic으로 message 전송
//        .then()
//        .map(ret -> true)
//        .onErrorResume(e -> {
//          System.out.println("Kafka send error");
//          return Mono.just(false);
//        });
//  }

//  public Flux<ReceiverRecord<String, String>> getTestTopicFlux() {
//    return testTopicStream;
//  }
//
//  private <T, G> Flux<ReceiverRecord<T, G>> createTopicCache(ReceiverOptions<T, G> receiverOptions, String topicName) {
//    ReceiverOptions<T, G> options = receiverOptions.subscription(Collections.singleton(topicName));
//
//    return KafkaReceiver.create(options).receive()
//        .doOnNext( System.out::println)
//        .cache();
//  }



}
