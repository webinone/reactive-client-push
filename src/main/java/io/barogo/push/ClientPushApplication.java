package io.barogo.push;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ClientPushApplication {

  public static void main(String[] args) {
    SpringApplication.run(ClientPushApplication.class, args);
  }

}
