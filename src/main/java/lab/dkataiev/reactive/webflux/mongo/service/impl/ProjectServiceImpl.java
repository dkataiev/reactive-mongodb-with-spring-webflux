package lab.dkataiev.reactive.webflux.mongo.service.impl;

import lab.dkataiev.reactive.webflux.mongo.model.Project;
import lab.dkataiev.reactive.webflux.mongo.model.Task;
import lab.dkataiev.reactive.webflux.mongo.repository.ProjectRepository;
import lab.dkataiev.reactive.webflux.mongo.repository.TaskRepository;
import lab.dkataiev.reactive.webflux.mongo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
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
}
