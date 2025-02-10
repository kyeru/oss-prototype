package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClients;
import com.oss_prototype.db_utils.RedisClientWrapper;
import com.oss_prototype.response.FinalReport;
import com.oss_prototype.response.FinalReport.ModelReportEntry;
import com.oss_prototype.models.ModelReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Service
@Slf4j
public class ReportService {
    private static final String REPORT_KEY_PREFIX = "report-";
    private static final long REPORT_TTL_SEC = 86400;

    private final ObjectMapper jsonMapper;
    private final RedisClientWrapper redisClient;

    private MongoOperations mongoTemplate;

    public ReportService(final RedisClientWrapper redisClient) {
        this.jsonMapper = new ObjectMapper();
        this.redisClient = redisClient;
        this.mongoTemplate = new MongoTemplate(
            new SimpleMongoClientDatabaseFactory(MongoClients.create(), "local"));
    }

    public String generateReport(final String token) {
        log.info("generate report for token {}", token);

        FinalReport finalReport = new FinalReport();
        finalReport.setToken(token);
        // 1. collect stored model reports
//        for (ModelName name : ModelName.values()) {
//            String reportKey = getReportKey(token, name.getName());
//            log.info("reading key {} from redis", reportKey);
//            String storedReport = redisClient.getValue(reportKey);
//            if (storedReport != null) {
//                reports.put(name.getName(), storedReport);
//            }
//        }

        List<ModelReport> reportList = mongoTemplate.query(ModelReport.class)
            .matching(query(where("token").is(token)))
            .all();
        List<ModelReportEntry> modelReportEntries = new ArrayList<>();
        for (ModelReport report : reportList) {
            log.info("adding {} report: {}", report.getModelName(), report.getReport());
            modelReportEntries.add(new ModelReportEntry(report.getModelName(), report.getReport()));
        }
        finalReport.setReports(modelReportEntries);

        // 2. make a response in json format
        try {
//            return jsonMapper.writeValueAsString(reports);
            return jsonMapper.writeValueAsString(finalReport);
        } catch (JsonProcessingException e) {
            log.error("json processing error: {}", finalReport, e);
            return null;
        }
    }

    public void storeReport(final ModelReport report) {
        String reportKey = getReportKey(report.getToken(), report.getModelName());
        redisClient.setValue(reportKey, report.getReport(), REPORT_TTL_SEC);
        mongoTemplate.update(ModelReport.class)
            .matching(query(where("token").is(report.getToken())
                    .and("modelName").is(report.getModelName())))
                .apply(update("report", report.getReport()))
                .upsert();
    }

    private String getReportKey(final String token, final String modelName) {
        return REPORT_KEY_PREFIX + token + "-" + modelName;
    }
}
