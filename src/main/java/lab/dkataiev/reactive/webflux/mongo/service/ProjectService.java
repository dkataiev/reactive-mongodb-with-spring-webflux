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

    Flux<Project> findByName(String name);

    Flux<Project> findByNameNot(String name);

    Flux<Project> findByNameLike(String name);

    Flux<Project> findByNameRegex(String name);

    Flux<Project> findByEstimatedCostGreaterThan(Long cost);

    Flux<Project> findByEstimatedCostBetween(Long from, Long to);

    Flux<Project> findByNameQuery(String name);

    Flux<Project> findByNameAndCostQuery(String name, Long cost);

    Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to);

    Flux<Project> findByNameRegexQuery(String name);

    Flux<Project> findByNameWithTemplate(String name);

    Flux<Project> findByEstimatedCostBetweenWithTemplate(Long from, Long to);

    Flux<Project> findByNameRegexWithTemplate(String name);

    Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost);

    Mono<Void> deleteByIdWithTemplate(String id);

    Mono<Long> findNumberOfProjectsCostGreaterThan(Long cost);

    Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(Long cost);

    Flux<ResultProjectTasks> findAllProjectTasks();

    Mono<Void> saveProjectAndTask(Mono<Project> p, Mono<Task> t);

    Mono<Void> saveProjectToGrid(Project p);

    Mono<Project> loadProjectFromGrid(String projectId);

    Mono<Void> deleteProjectFromGrid(String projectId);

}
