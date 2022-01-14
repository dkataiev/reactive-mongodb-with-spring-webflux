package lab.dkataiev.reactive.webflux.mongo.repository;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {
}
