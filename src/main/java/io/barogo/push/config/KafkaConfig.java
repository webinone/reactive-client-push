package io.barogo.push.config;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaConfig {

  @Value("${kafka.host}")
  private String host;

  @Value("${kafka.topic.test.websocket}")
  private String testWebsocketTopic;

  @Value("${kafka.groupid}")
  private String groupId;

  @Bean("kafkaSender")
  public KafkaSender<String, Object> kafkaSender() {
    SenderOptions<String, Object> senderOptions = SenderOptions.create(getProducerProps());
    senderOptions.scheduler(Schedulers.parallel());
    senderOptions.closeTimeout(Duration.ofSeconds(5));

    return KafkaSender.create(senderOptions);
  }

  @Bean(name = "receiverObjectOptions")
  public ReceiverOptions<String, Object> receiverObjectOptions() {
    return ReceiverOptions.<String, Object>create(getConsumerProps())
        .subscription(Collections.singleton(testWebsocketTopic));
  }

  @Bean(name = "receiverStringOptions")
  public ReceiverOptions<String, String> receiverStringOptions() {
    return ReceiverOptions.<String, String>create(getConsumerProps())
        .subscription(Collections.singleton(testWebsocketTopic));
  }

  private Map<String, Object> getProducerProps() {
    return new HashMap<>() {{
      put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
      put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
    }};
  }

  private Map<String, Object> getConsumerProps() {
    return new HashMap<>() {{
      put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
      put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    }};
  }

}
