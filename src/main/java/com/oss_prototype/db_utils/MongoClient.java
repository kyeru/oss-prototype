package com.oss_prototype.db_utils;

import com.mongodb.client.MongoClients;
import com.oss_prototype.models.ModelReport;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Component
public class MongoClient {
    private MongoOperations mongoTemplate;

    public MongoClient() {
        this.mongoTemplate = new MongoTemplate(
            new SimpleMongoClientDatabaseFactory(MongoClients.create(), "local"));
    }

    public List<ModelReport> fetchModelReports(final String token) {
        List<ModelReport> reportList = mongoTemplate.query(ModelReport.class)
            .matching(query(where("token").is(token)))
            .all();
        return reportList;
    }

    public void storeModelReport(final String token, final String modelName, final String report) {
        mongoTemplate.update(ModelReport.class)
            .matching(query(where("token").is(token).and("modelName").is(modelName)))
            .apply(update("report", report))
            .upsert();
    }
}
