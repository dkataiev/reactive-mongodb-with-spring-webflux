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
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createProject)
                .andRoute(RequestPredicates.POST("/projects/createTask")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::createTask)
                .andRoute(RequestPredicates.GET("/projects")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findAll)
                .andRoute(RequestPredicates.GET("/projects/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(RequestPredicates.DELETE("/projects/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::deleteById);
    }

}
