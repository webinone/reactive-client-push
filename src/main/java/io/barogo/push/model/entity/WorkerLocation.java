package io.barogo.push.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("worker_location")
public class WorkerLocation {

  @Id
  private Long id;

  private String longitude;

  private String altitude;

  private String address;
}
