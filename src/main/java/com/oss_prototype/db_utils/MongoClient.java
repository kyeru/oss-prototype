package com.oss_prototype.db_utils;

import com.mongodb.client.MongoClients;
import com.oss_prototype.models.TaskResponseMessage;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Query;
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

    public List<TaskResponseMessage> fetchModelReports(final String token) {
        List<TaskResponseMessage> reportList = mongoTemplate.query(TaskResponseMessage.class)
            .matching(query(where("token").is(token)))
            .all();
        return reportList;
    }

    public List<TaskResponseMessage> fetchAllModelReports(final int limit) {
        List<TaskResponseMessage> reportList = mongoTemplate
            .find(new Query().limit(limit), TaskResponseMessage.class);
        return reportList;
    }

    public void storeModelReport(final TaskResponseMessage report) {
        mongoTemplate.update(TaskResponseMessage.class)
            .matching(
                query(where("token").is(report.getToken())
                    .and("modelName").is(report.getModelName()))
            )
            .apply(
                update("report", report.getReport())
//                    .set("status", report.getStatus())
                    .set("taskStartTime", report.getTaskStartTime())
                    .set("taskEndTime", report.getTaskEndTime())
            )
            .upsert();
    }
}
