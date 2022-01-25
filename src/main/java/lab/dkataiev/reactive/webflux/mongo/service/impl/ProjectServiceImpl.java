package lab.dkataiev.reactive.webflux.mongo.service.impl;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import lab.dkataiev.reactive.webflux.mongo.model.Task;
import lab.dkataiev.reactive.webflux.mongo.repository.ProjectRepository;
import lab.dkataiev.reactive.webflux.mongo.repository.TaskRepository;
import lab.dkataiev.reactive.webflux.mongo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, ReactiveMongoTemplate mongoTemplate) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Project> createOrUpdateProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Mono<Task> createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Flux<Project> findAll() {
        return projectRepository.findAll();
    }

    @Override
    public Mono<Project> findById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return projectRepository.deleteById(id);
    }

    @Override
    public Flux<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }

    @Override
    public Flux<Project> findByNameNot(String name) {
        return projectRepository.findByNameNot(name);
    }

    @Override
    public Flux<Project> findByNameLike(String name) {
        return projectRepository.findByNameLike(name);
    }

    @Override
    public Flux<Project> findByNameRegex(String name) {
        return projectRepository.findByNameRegex(name);
    }

    @Override
    public Flux<Project> findByEstimatedCostGreaterThan(Long cost) {
        return projectRepository.findByEstimatedCostGreaterThan(cost);
    }

    @Override
    public Flux<Project> findByEstimatedCostBetween(Long from, Long to) {
        return projectRepository.findByEstimatedCostBetween(from, to);
    }

    @Override
    public Flux<Project> findByNameQuery(String name) {
        return projectRepository.findByNameQuery(name);
    }

    @Override
    public Flux<Project> findByNameAndCostQuery(String name, Long cost) {
        return projectRepository.findByNameAndCostQuery(name, cost);
    }

    @Override
    public Flux<Project> findByEstimatedCostBetweenQuery(Long from, Long to) {
        return projectRepository.findByEstimatedCostBetweenQuery(from, to, Sort.by(Sort.Direction.DESC, "cost"));
    }

    @Override
    public Flux<Project> findByNameRegexQuery(String name) {
        return projectRepository.findByNameRegexQuery(name);
    }

    @Override
    public Flux<Project> findByNameWithTemplate(String name) {
        return mongoTemplate.find(Query.query(Criteria.where("name").is(name)), Project.class);
    }

    @Override
    public Flux<Project> findByEstimatedCostBetweenWithTemplate(Long from, Long to) {
        Query query = Query.query(Criteria.where("cost").gt(from).lt(to));
        query.with(Sort.by(Sort.Direction.DESC, "cost"));
        return mongoTemplate.find(query, Project.class);
    }

    @Override
    public Flux<Project> findByNameRegexWithTemplate(String name) {
        return mongoTemplate.find(Query.query(Criteria.where("name").regex(name)), Project.class);
    }

    @Override
    public Mono<Void> upsertCostWithCriteriaTemplate(String id, Long cost) {
        return mongoTemplate.upsert(
                Query.query(Criteria.where("_id").is(id)),
                Update.update("cost", cost), Project.class).then();
    }

    @Override
    public Mono<Void> deleteByIdWithTemplate(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.remove(query, Project.class).then();
    }
}
