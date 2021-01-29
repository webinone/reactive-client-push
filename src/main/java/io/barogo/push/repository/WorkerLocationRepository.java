package io.barogo.push.repository;

import io.barogo.push.model.entity.WorkerLocation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerLocationRepository extends ReactiveCrudRepository<WorkerLocation, Long> {
}
