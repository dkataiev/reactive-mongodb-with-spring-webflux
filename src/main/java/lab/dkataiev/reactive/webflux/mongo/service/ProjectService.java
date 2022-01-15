package lab.dkataiev.reactive.webflux.mongo.service;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import lab.dkataiev.reactive.webflux.mongo.model.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProjectService {

    Mono<Project> createOrUpdateProject(Project project);

    Mono<Task> createTask(Task task);

    Flux<Project> findAll();

    Mono<Project> findById(String id);

    Mono<Void> deleteById(String id);

}
