package io.barogo.push.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
@EnableR2dbcRepositories(basePackages = "io.barogo.push.repository")
public class R2DBCConfig {

//  @Bean
//  public PostgresqlConnectionFactory connectionFactory() {
//    return new PostgresqlConnectionFactory(
//        PostgresqlConnectionConfiguration.builder()
//            .host("localhost")
//            .database("zoo")
//            .username("zoo")
//            .password("zoo").build());
//  }

  @Bean
  public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("schema.sql"));
    initializer.setDatabasePopulator(populator);
    return initializer;
  }

}
