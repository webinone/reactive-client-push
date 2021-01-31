package io.barogo.push.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.barogo.push.model.dto.KafkaMessage;
import io.barogo.push.publisher.EchoPublisher;
import io.barogo.push.service.EchoService;
import io.barogo.push.service.KafkaService;
import io.barogo.push.service.RedisPubSubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
//@AllArgsConstructor
public class RedisPushSubSocketHandler implements WebSocketHandler {

  private static final ObjectMapper json = new ObjectMapper();
  private final RedisPubSubService redisPubSubService;

  @Override
  public Mono<Void> handle(WebSocketSession session) {

    session
        .receive()
        .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
        .doOnNext(message -> redisPubSubService.postRedisPub(message).subscribe())
        .subscribe();

    return session.send(redisPubSubService.getTestTopicFlux()
        .map(record ->
            "Redis push receive, " + record
        )
        .map(session::textMessage));
//        .and(session.receive()
//            .map(WebSocketMessage::getPayloadAsText));
  }
}
