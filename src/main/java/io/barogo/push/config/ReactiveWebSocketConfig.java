package io.barogo.push.config;

import io.barogo.push.handler.EchoSocketHandler;
import io.barogo.push.handler.KafkaSocketHandler;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

@Configuration
public class ReactiveWebSocketConfig {

  @Autowired
  EchoSocketHandler echoSocketHandler;

  @Autowired
  KafkaSocketHandler kafkaSocketHandler;

  @Bean
  public HandlerMapping handlerMapping() {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/echo", echoSocketHandler);
    map.put("/kafka", kafkaSocketHandler);
//    map.put("/chat", new ChatSocketHandler(messagePublisher, messages));
//    int order = -1; // before annotated controllers

//    return new SimpleUrlHandlerMapping(map, order);
//    map.put("/echo", echoHandler());

    SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
    mapping.setUrlMap(map);
    mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return mapping;
  }

  @Bean
  public WebSocketHandlerAdapter handlerAdapter() {
    return new WebSocketHandlerAdapter(webSocketService());
  }

  @Bean
  public WebSocketService webSocketService() {
//    return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
    return new CustomHandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
  }

}
