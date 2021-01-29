package io.barogo.push.service;

import io.barogo.push.model.entity.WorkerLocation;
import io.barogo.push.repository.WorkerLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class WorkerLocationService {
  private final WorkerLocationRepository workerLocationRepository;

  public Flux<WorkerLocation> readWorkerLocations() {
    return this.workerLocationRepository.findAll();
  }
}
