package lab.dkataiev.reactive.webflux.mongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "projects")
public class Project {

    @Id
    private String _id;

    private String name;

    @Field("desc")
    private String description;

    private String startDate;

    private String endDate;

    @Field("cost")
    private long estimatedCost;

    private List<String> countryList;

    @Version
    private long version;

}
