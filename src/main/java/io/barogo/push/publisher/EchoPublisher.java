package io.barogo.push.publisher;

import io.vavr.control.Try;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

@Component
@Slf4j
public class EchoPublisher implements Consumer<FluxSink<String>> {

  private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
  private final Executor executor = Executors.newSingleThreadExecutor();

  public boolean push(String greeting) {
    return queue.offer(greeting);
  }

  @Override
  public void accept(FluxSink<String> sink) {

    this.executor.execute(() -> {

      while (true) {
        Try.of(() -> {
          final String greeting = queue.take();
          return sink.next(greeting);
        })
            .onFailure(ex -> log.error("Could not take greeting from queue", ex));
      }
    });
  }
}
