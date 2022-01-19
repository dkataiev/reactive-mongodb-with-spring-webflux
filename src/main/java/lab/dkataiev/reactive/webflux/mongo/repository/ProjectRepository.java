package lab.dkataiev.reactive.webflux.mongo.repository;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {

    Flux<Project> findByName(String name);

    Flux<Project> findByNameNot(String name);

    Flux<Project> findByNameLike(String name);

    Flux<Project> findByNameRegex(String name);

    Flux<Project> findByEstimatedCostGreaterThan(Long cost);

    Flux<Project> findByEstimatedCostBetween(Long from, Long to);

}
