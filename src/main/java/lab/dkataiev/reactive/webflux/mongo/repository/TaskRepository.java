package lab.dkataiev.reactive.webflux.mongo.repository;

import lab.dkataiev.reactive.webflux.mongo.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
}
