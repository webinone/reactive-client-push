package io.barogo.push.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.barogo.push.model.dto.EchoMessage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EchoService {

  private final ReactiveRedisOperations<String, String> reactiveRedisOperations;
  private ObjectMapper jsonMapper = new ObjectMapper();

  public String greeting(String message)  {

    // business logic 처리...
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

}
