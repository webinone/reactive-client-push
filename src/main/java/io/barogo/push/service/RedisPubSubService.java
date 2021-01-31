package io.barogo.push.service;

import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubSubService {

  private final ReactiveRedisMessageListenerContainer reactiveMsgListenerContainer;
  private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
  private final ChannelTopic topic;

  private Flux<String> topicStream;

  @PostConstruct
  public void init() throws IOException {    // Consumer를 열어놓음

    log.info(">>>>>>>>>>> redis topic Init !!!!");
    log.info(">>>>>>>>>>> topic name : " + topic.getTopic());

    topicStream = createTopicCache(reactiveMsgListenerContainer, topic);
  }

  public Mono<Long> postRedisPub(String message) {
    log.info("New pub message '" + message + "' send to Channel '" + topic.getTopic() + "'.");
    return reactiveRedisTemplate.convertAndSend(topic.getTopic(), message);
  }

  public Flux<String> getTestTopicFlux() {
    return topicStream;
  }

  private Flux<String> createTopicCache(ReactiveRedisMessageListenerContainer reactiveMsgListenerContainer, ChannelTopic topic) {
    return reactiveMsgListenerContainer
        .receive(topic)
        .map(ReactiveSubscription.Message::getMessage)
        .map(msg -> {
          log.info("New Message received: '" + msg.toString() + "'.");
          return msg.toString() + "\n";
        });
  }
}
