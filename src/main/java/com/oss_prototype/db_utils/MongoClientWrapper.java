package com.oss_prototype.db_utils;

import com.mongodb.client.MongoClient;
import com.oss_prototype.models.TaskResponseMessage;
import com.oss_prototype.request.TaskRequest;
import com.oss_prototype.response.RequestHistory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Component
public class MongoClientWrapper {
    private MongoOperations mongoTemplate;

    public MongoClientWrapper(MongoClient mongoClient) {
        this.mongoTemplate = new MongoTemplate(mongoClient, "oss");
    }

    public List<TaskResponseMessage> fetchModelReports(final String token) {
        List<TaskResponseMessage> responseList = mongoTemplate.query(TaskResponseMessage.class)
            .matching(query(where("token").is(token)))
            .all();
        return responseList;
    }

    public List<TaskResponseMessage> fetchAllModelReports(final int limit) {
        List<TaskResponseMessage> responseList = mongoTemplate
            .find(new Query().limit(limit), TaskResponseMessage.class);
        return responseList;
    }

    public RequestHistory fetchRequestHistory(final String user) {
        RequestHistory requestHistory = mongoTemplate.query(RequestHistory.class)
            .matching(query(where("user").is(user)))
            .firstValue();
        return requestHistory;
    }

    public void storeTaskReport(final TaskRequest request, final String token) {
        mongoTemplate.update(RequestHistory.class)
            .matching(
                query(where("token").is(token))
            )
            .apply(
                update("version", request.getVersion())
                    .set("requestTime", request.getTimestamp())
                    .set("token", token)
                    .set("user", request.getMetadata().getUser())
                    .set("taskType", request.getMetadata().getTaskType())
            )
            .upsert();
    }

    public void storeModelReport(final TaskResponseMessage response) {
        mongoTemplate.update(TaskResponseMessage.class)
            .matching(
                query(where("token").is(response.getToken())
                    .and("modelName").is(response.getModelName()))
            )
            .apply(
                update("modelVersion", response.getModelVersion())
                    .set("returnCode", response.getReturnCode())
                    .set("errorMessage", response.getErrorMessage())
                    .set("taskStartTime", response.getTaskStartTime())
                    .set("taskEndTime", response.getTaskEndTime())
                    .set("report", response.getReport())
            )
            .upsert();
    }
}
