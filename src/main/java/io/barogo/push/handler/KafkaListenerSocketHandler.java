package io.barogo.push.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.barogo.push.model.dto.KafkaMessage;
import io.barogo.push.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KafkaListenerSocketHandler implements WebSocketHandler {

  private static final ObjectMapper json = new ObjectMapper();

  private final KafkaService kafkaService;

  public KafkaListenerSocketHandler(KafkaService kafkaService) {
    this.kafkaService = kafkaService;
  }

  @Override
  public Mono<Void> handle(WebSocketSession webSocketSession) {
    return webSocketSession.send(kafkaService.getTestTopicFlux()
        .map(record -> {
          KafkaMessage message = new KafkaMessage("[Test] Add message", record.value());

          try {
            return json.writeValueAsString(message);
          } catch (JsonProcessingException e) {
            return "Error while serializing to JSON";
          }
        })
        .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
        .map(WebSocketMessage::getPayloadAsText));
  }
}
