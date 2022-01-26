package lab.dkataiev.reactive.webflux.mongo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "tasks")
public class Task {

    @Id
    private String _id;

    @Field("pid")
    private String projectId;

    private String name;

    @Field("desc")
    private String description;

    private String ownerName;

    private long cost;

    @Version
    private long version;
}
