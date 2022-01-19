package lab.dkataiev.reactive.webflux.mongo.router;

import lab.dkataiev.reactive.webflux.mongo.handler.ProjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProjectRouter {

    @Bean
    public RouterFunction<ServerResponse> routeProjects(ProjectHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.POST("/projects/create")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createOrUpdateProject)
                .andRoute(RequestPredicates.POST("/projects/createTask")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createTask)
                .andRoute(RequestPredicates.GET("/projects")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(RequestPredicates.GET("/projects/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(RequestPredicates.POST("/projects/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createOrUpdateProject)
                .andRoute(RequestPredicates.DELETE("/projects/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteById)

                .andRoute(RequestPredicates.GET("/projects/find/byName")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByName)
                .andRoute(RequestPredicates.GET("/projects/find/byNameNot")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameNot)
                .andRoute(RequestPredicates.GET("/projects/find/byNameLike")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameLike)
                .andRoute(RequestPredicates.GET("/projects/find/byNameRegex")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameRegex)
                .andRoute(RequestPredicates.GET("/projects/find/byEstimatedCostGreaterThan")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostGreaterThan)
                .andRoute(RequestPredicates.GET("/projects/find/byEstimatedCostBetween")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostBetween)

                .andRoute(RequestPredicates.GET("/projects/query/byName")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameQuery)
                .andRoute(RequestPredicates.GET("/projects/query/byNameAndCost")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameAndCostQuery)
                .andRoute(RequestPredicates.GET("/projects/query/byEstimatedCostBetween")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByEstimatedCostBetweenQuery)
                .andRoute(RequestPredicates.GET("/projects/query/byNameRegex")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findByNameRegexQuery)
                ;
    }

}
