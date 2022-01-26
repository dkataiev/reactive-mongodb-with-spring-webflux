package lab.dkataiev.reactive.webflux.mongo.handler;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import lab.dkataiev.reactive.webflux.mongo.model.Task;
import lab.dkataiev.reactive.webflux.mongo.service.ProjectService;
import lab.dkataiev.reactive.webflux.mongo.service.ResultByStartDateAndCost;
import lab.dkataiev.reactive.webflux.mongo.service.ResultProjectTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProjectHandler {

    private final ProjectService projectService;

    @Autowired
    public ProjectHandler(ProjectService projectService) {
        this.projectService = projectService;
    }

    public Mono<ServerResponse> createOrUpdateProject(ServerRequest serverRequest) {
        Mono<Project> project = serverRequest.bodyToMono(Project.class);
        return project.flatMap(projectService::createOrUpdateProject)
                .flatMap(data -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data));
//                .onErrorResume(error -> {
//                    if (error instanceof OptimisticLockingFailureException) {
//                        return ServerResponse.badRequest().build();
//                    }
//                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                });
    }

    public Mono<ServerResponse> createTask(ServerRequest serverRequest) {
        Mono<Task> task = serverRequest.bodyToMono(Task.class);
        return task.flatMap(projectService::createTask)
                .flatMap(data -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(data));
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(projectService.findAll(), Project.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return projectService.findById(id)
                .flatMap(data -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(data))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(projectService.deleteById(id), Void.class).log();
    }

    public Mono<ServerResponse> findByName(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByName(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameNot(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameNot(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameLike(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameLike(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameRegex(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        String regex = "^" + name;
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameRegex(regex), Project.class).log();
    }

    public Mono<ServerResponse> findByEstimatedCostGreaterThan(ServerRequest serverRequest) {
        String cost = serverRequest.queryParam("cost").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByEstimatedCostGreaterThan(Long.valueOf(cost)), Project.class).log();
    }

    public Mono<ServerResponse> findByEstimatedCostBetween(ServerRequest serverRequest) {
        String from = serverRequest.queryParam("from").get();
        String to = serverRequest.queryParam("to").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByEstimatedCostBetween(Long.valueOf(from), Long.valueOf(to)), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByNameQuery(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameQuery(name), Project.class).log();
    }

    public Mono<ServerResponse> findByNameAndCostQuery(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        String cost = serverRequest.queryParam("cost").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameAndCostQuery(name, Long.valueOf(cost)), Project.class).log();
    }

    public Mono<ServerResponse> findByEstimatedCostBetweenQuery(ServerRequest serverRequest) {
        String from = serverRequest.queryParam("from").get();
        String to = serverRequest.queryParam("to").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByEstimatedCostBetweenQuery(Long.valueOf(from), Long.valueOf(to)), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByNameRegexQuery(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        String regex = "^" + name;
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameRegexQuery(regex), Project.class).log();
    }

    public Mono<ServerResponse> findByNameQueryWithTemplate(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameWithTemplate(name), Project.class).log();
    }

    public Mono<ServerResponse> findByEstimatedCostBetweenWithTemplate(ServerRequest serverRequest) {
        String from = serverRequest.queryParam("from").get();
        String to = serverRequest.queryParam("to").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByEstimatedCostBetweenWithTemplate(Long.valueOf(from), Long.valueOf(to)), Project.class)
                .log();
    }

    public Mono<ServerResponse> findByNameRegexWithTemplate(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").get();
        String regex = "^" + name;
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findByNameRegexWithTemplate(regex), Project.class).log();
    }

    public Mono<ServerResponse> upsertCostWithCriteriaTemplate(ServerRequest serverRequest) {
        String id = serverRequest.queryParam("id").get();
        String cost = serverRequest.queryParam("cost").get();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.upsertCostWithCriteriaTemplate(id, Long.valueOf(cost)), Void.class).log();

    }

    public Mono<ServerResponse> deleteByIdWithTemplate(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(projectService.deleteByIdWithTemplate(id), Void.class).log();
    }

    public Mono<ServerResponse> findNumberOfProjectsCostGreaterThan(ServerRequest serverRequest) {
        String cost = serverRequest.queryParam("cost").get();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findNumberOfProjectsCostGreaterThan(Long.valueOf(cost)), Long.class).log();
    }

    public Mono<ServerResponse> findCostsGroupByStartDateForProjectsCostGreaterThan(ServerRequest serverRequest) {
        String cost = serverRequest.queryParam("cost").get();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findCostsGroupByStartDateForProjectsCostGreaterThan(Long.valueOf(cost)),
                        ResultByStartDateAndCost.class).log();
    }

    public Mono<ServerResponse> findAllProjectTasks(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.findAllProjectTasks(), ResultProjectTasks.class).log();
    }

    // [TODO] #1 Does not work as a transaction. If project can be created and task fails - project is still created.
    public Mono<ServerResponse> saveProjectAndTask(ServerRequest serverRequest) {
        Project project = Project.builder()._id("8").name("Project8").build();
        Task task = Task.builder()._id("10").projectId("6").build();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(projectService.saveProjectAndTask(Mono.just(project), Mono.just(task)), Void.class).log();
    }
}
