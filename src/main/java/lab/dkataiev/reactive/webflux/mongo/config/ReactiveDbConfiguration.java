package lab.dkataiev.reactive.webflux.mongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableTransactionManagement
@EnableReactiveMongoRepositories(basePackages = "lab.dkataiev.reactive.webflux.mongo.repository")
public class ReactiveDbConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${mongodb.replicaset.name}")
    private String replicasetName;

    @Value("${mongodb.replicaset.username}")
    private String replicasetUserName;

    @Value("${mongodb.replicaset.password}")
    private String replicasetPassword;

    @Value("${mongodb.replicaset.primary}")
    private String replicasetPrimary;

    @Value("${mongodb.replicaset.port}")
    private String replicasetPort;

    @Value("${mongodb.replicaset.database}")
    private String databaseName;

    @Value("${mongodb.replicaset.authentication-database}")
    private String replicasetAuthenticationDatabase;

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create("mongodb://" + replicasetUserName + ":" + replicasetPassword +
                "@" + replicasetPrimary + ":" + replicasetPort + "/" + databaseName +
                "?replicaSet=" + replicasetName + "&authSource=" + replicasetAuthenticationDatabase);
    }

    @Bean
    @Override
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory databaseFactory, MappingMongoConverter mongoConverter) {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    @Bean
    public ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTransactionManager(factory);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
}
