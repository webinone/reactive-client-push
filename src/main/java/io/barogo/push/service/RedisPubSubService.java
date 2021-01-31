package io.barogo.push.service;

import java.io.IOException;
import java.util.Collections;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubSubService {

  private final ReactiveRedisMessageListenerContainer reactiveMsgListenerContainer;
  private final ChannelTopic topic;

  private Flux<String> topicStream;

  @PostConstruct
  public void init() throws IOException {    // Consumer를 열어놓음

    log.info(">>>>>>>>>>> redis topic Init !!!!");
    log.info(">>>>>>>>>>> topic name : " + topic.getTopic());

    topicStream = createTopicCache(reactiveMsgListenerContainer, topic);
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
