package io.barogo.push.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.barogo.push.model.dto.KafkaMessageDto;
import io.barogo.push.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KafkaProduceConsumeSocketHandler implements WebSocketHandler {

  private static final ObjectMapper json = new ObjectMapper();

  private final KafkaService kafkaService;

  public KafkaProduceConsumeSocketHandler(KafkaService kafkaService) {
    this.kafkaService = kafkaService;
  }

  @Override
  public Mono<Void> handle(WebSocketSession session) {

    session
        .receive()
        .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
        .doOnNext(echoMessage -> kafkaService.sendKafka("test", echoMessage).subscribe())
        .subscribe();

    return session.send(kafkaService.getTestTopicFlux()
        .map(record -> {
          KafkaMessageDto message = new KafkaMessageDto("[Test] Add message", record.value());

          try {
            return json.writeValueAsString(message);
          } catch (JsonProcessingException e) {
            return "Error while serializing to JSON";
          }
        })
        .map(session::textMessage));
  }
}
