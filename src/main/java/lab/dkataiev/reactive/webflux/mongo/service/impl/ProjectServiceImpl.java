package lab.dkataiev.reactive.webflux.mongo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Bytes;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lab.dkataiev.reactive.webflux.mongo.model.Project;
import lab.dkataiev.reactive.webflux.mongo.model.Task;
import lab.dkataiev.reactive.webflux.mongo.repository.ProjectRepository;
import lab.dkataiev.reactive.webflux.mongo.repository.TaskRepository;
import lab.dkataiev.reactive.webflux.mongo.service.ProjectService;
import lab.dkataiev.reactive.webflux.mongo.service.ResultByStartDateAndCost;
import lab.dkataiev.reactive.webflux.mongo.service.ResultCount;
import lab.dkataiev.reactive.webflux.mongo.service.ResultProjectTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.CountOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsResource;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ReactiveMongoTemplate mongoTemplate;
    private final ReactiveGridFsTemplate gridFsTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository,
                              ReactiveMongoTemplate mongoTemplate, ReactiveGridFsTemplate gridFsTemplate) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.mongoTemplate = mongoTemplate;
        this.gridFsTemplate = gridFsTemplate;

        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    @Override
    public Mono<Long> findNumberOfProjectsCostGreaterThan(Long cost) {
        MatchOperation matchStage = Aggregation.match(Criteria.where("cost").gt(cost));
        CountOperation countStage = Aggregation.count().as("costly_projects");

        Aggregation aggregation = Aggregation.newAggregation(matchStage, countStage);
        Flux<ResultCount> resultFlux = mongoTemplate.aggregate(aggregation, "projects", ResultCount.class);
        Flux<Long> longFlux = resultFlux.map(ResultCount::getCostly_projects).switchIfEmpty(Flux.just(0L));
        return longFlux.take(1).single();
    }

    @Override
    public Flux<ResultByStartDateAndCost> findCostsGroupByStartDateForProjectsCostGreaterThan(Long cost) {
        MatchOperation matchStage = Aggregation.match(Criteria.where("cost").gt(cost));
        GroupOperation groupStage = Aggregation.group("startDate").sum("cost").as("total");
        SortOperation sortByTotalStage = Aggregation.sort(Sort.by(Sort.Direction.DESC, "total"));

        Aggregation aggregation = Aggregation.newAggregation(matchStage, groupStage, sortByTotalStage);
        return mongoTemplate.aggregate(aggregation, "projects", ResultByStartDateAndCost.class);
    }

    @Override
    public Flux<ResultProjectTasks> findAllProjectTasks() {
        LookupOperation lookupOperation = LookupOperation.newLookup().from("tasks")
                .localField("_id").foreignField("pid")
                .as("ProjectsTasks");

        UnwindOperation unwindOperation = Aggregation.unwind("ProjectsTasks");
        ProjectionOperation projectionOperation = Aggregation.project()
                .andExpression("_id").as("id")
                .andExpression("name").as("name")
                .andExpression("ProjectsTasks.name").as("taskName")
                .andExpression("ProjectsTasks.ownerName").as("taskOwnerName");
        Aggregation aggregation = Aggregation.newAggregation(lookupOperation, unwindOperation, projectionOperation);
        return mongoTemplate.aggregate(aggregation, "projects", ResultProjectTasks.class);
    }

    @Override
    @Transactional
    public Mono<Void> saveProjectAndTask(Mono<Project> p, Mono<Task> t) {
        return p.flatMap(projectRepository::save)
                .then(t).flatMap(taskRepository::save)
                .then();
    }

    @Override
    public Mono<Void> saveProjectToGrid(Project p) {
        String s = serializeToJson(p);
        byte[] serialized = s.getBytes();

        DBObject metaData = new BasicDBObject();
        metaData.put("projectId", p.get_id());
        DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
        DefaultDataBuffer dataBuffer = factory.wrap(serialized);
        Flux<DataBuffer> body = Flux.just(dataBuffer);
        return gridFsTemplate.store(body, p.get_id(), metaData).then();
    }

    @Override
    // TODO: #2 Fix method.
    public Mono<Project> loadProjectFromGrid(String projectId) {
        Mono<GridFSFile> file = gridFsTemplate.findOne(
                Query.query(Criteria.where("metadata.projectId").is(projectId))
                        .with(Sort.by(Sort.Direction.DESC, "uploadDate"))
                        .limit(1));

        Flux<byte[]> byteSeq = file.flatMap(gridFsTemplate::getResource)
                .flatMapMany(ReactiveGridFsResource::getDownloadStream)
                .map(buffer -> {
                    byte[] b = new byte[buffer.readableByteCount()];
                    buffer.read(b);
                    return b;
                });

        return byteSeq.collectList().flatMap(bytes -> {
            byte[] data = Bytes.concat(bytes.toArray(new byte[bytes.size()][]));
            String s = new String(data, StandardCharsets.UTF_8);
            return Mono.just(deserializeFromJson(s));
        });
    }

    @Override
    public Mono<Void> deleteProjectFromGrid(String projectId) {
        return gridFsTemplate.delete(Query.query(Criteria.where("metadata.projectId").is(projectId)));
    }

    private String serializeToJson(Project p) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(p);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Project deserializeFromJson(String json) {
        try {
            return objectMapper.readValue(json, Project.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
