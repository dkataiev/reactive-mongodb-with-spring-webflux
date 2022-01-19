package lab.dkataiev.reactive.webflux.mongo.repository;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {

    Flux<Project> findByName(String name);

    Flux<Project> findByNameNot(String name);

    Flux<Project> findByNameLike(String name);

    Flux<Project> findByNameRegex(String name);

    Flux<Project> findByEstimatedCostGreaterThan(Long cost);

    Flux<Project> findByEstimatedCostBetween(Long from, Long to);

    @Query("{'name' : ?0}")
    Flux<Project> findByNameQuery(String name);

    @Query("{'name' : ?0, 'cost' : ?1}")
    Flux<Project> findByNameAndCostQuery(String name, Long cost);

    @Query("{cost : {$gt : ?0, $lt : ?1}}")
    Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to, Sort sort);

    @Query(value = "{'name' : { $regex: ?0 }}", fields = "{'name' : 1, 'cost' : 1}")
    Flux<Project> findByNameRegexQuery(String name);
}
