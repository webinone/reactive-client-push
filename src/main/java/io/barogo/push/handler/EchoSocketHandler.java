package io.barogo.push.handler;

import io.barogo.push.model.dto.EchoMessage;
import io.barogo.push.publisher.EchoPublisher;
import io.barogo.push.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
//@AllArgsConstructor
public class EchoSocketHandler implements WebSocketHandler {

  private final EchoService echoService;
  private final EchoPublisher echoPublisher;
  private final Flux<String> publisher;

  public EchoSocketHandler(EchoService echoService, EchoPublisher echoPublisher) {
    this.echoService = echoService;
    this.echoPublisher = echoPublisher;
    this.publisher = Flux.create(echoPublisher).share();
  }

  /**
   * Echo test
   * */
  @Override
  public Mono<Void> handle(WebSocketSession session) {
    log.info("new connection: {}", session.getId());
    session
        .receive()
        .map(webSocketMessage -> webSocketMessage.getPayloadAsText())
        .map(echoMessage -> echoService.greeting(echoMessage))
        .doOnNext(echoMessage -> echoPublisher.push(echoMessage))
        .subscribe();

    Flux<WebSocketMessage> message =
        publisher.map(echoMessage -> session.textMessage(echoMessage));

    return session.send(message);
  }
}
