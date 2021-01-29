package io.barogo.push.config;

import io.barogo.push.common.security.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class CustomHandshakeWebSocketService extends HandshakeWebSocketService {

  public CustomHandshakeWebSocketService(
      RequestUpgradeStrategy upgradeStrategy) {
    super(upgradeStrategy);
  }

  @Override
  public Mono<Void> handleRequest(ServerWebExchange exchange, WebSocketHandler handler) {

    ServerHttpRequest request =  exchange.getRequest();

    log.info(">>>>>>>>>>>>>>> CustomHandshakeWebSocketService.handleRequest !!!!!!");
    log.info(">>>>>>>>>>>>>>> request.toString() : " + request.toString());
    log.info(">>>>>>>>>>>>>>> request.getHeaders().toString() : " + request.getHeaders().toString());
    log.info(">>>>>>>>>>>>>>> request.getQueryParams().toString() : " + request.getQueryParams().toString());

    if (request.getQueryParams().containsKey("token")) {
      if (!JwtTokenUtils.validateToken(request.getQueryParams().get("token").get(0))) {
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));
      }
      return super.handleRequest(exchange, handler);
    } else {
      return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request"));
    }
  }
}
