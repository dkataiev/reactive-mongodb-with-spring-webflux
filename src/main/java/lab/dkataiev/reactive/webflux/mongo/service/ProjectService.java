package lab.dkataiev.reactive.webflux.mongo.service;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import lab.dkataiev.reactive.webflux.mongo.model.Task;
import reactor.core.publisher.Mono;

public interface ProjectService {

    Mono<Project> createProject(Project project);

    Mono<Task> createTask(Task task);

}
