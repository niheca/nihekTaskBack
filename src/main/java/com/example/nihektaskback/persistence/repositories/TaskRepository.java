package com.example.nihektaskback.persistence.repositories;

import com.example.nihektaskback.persistence.entities.TaskEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<TaskEntity, String> {


}
