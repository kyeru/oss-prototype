package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.db_utils.MongoClient;
import com.oss_prototype.db_utils.RedisClientWrapper;
import com.oss_prototype.response.FinalReport;
import com.oss_prototype.response.FinalReport.ModelReportEntry;
import com.oss_prototype.models.TaskResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ReportService {
    private static final String REPORT_KEY_PREFIX = "report-";
    private static final long REPORT_TTL_SEC = 86400;

    private final ObjectMapper jsonMapper;
    private final RedisClientWrapper redisClient;

    @Autowired
    private MongoClient mongoClient;

    public ReportService(final RedisClientWrapper redisClient) {
        this.jsonMapper = new ObjectMapper();
        this.redisClient = redisClient;
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

        List<TaskResponseMessage> reportList = mongoClient.fetchModelReports(token);
        List<ModelReportEntry> modelReportEntries = new ArrayList<>();
        for (TaskResponseMessage report : reportList) {
            log.info("adding {} report: {}", report.getModelName(), report.getReport());
            modelReportEntries.add(new ModelReportEntry(report.getModelName(), report.getReport()));
        }
        finalReport.setReports(modelReportEntries);

        // 2. make a response in json format
        try {
            return jsonMapper.writeValueAsString(finalReport);
        } catch (JsonProcessingException e) {
            log.error("json processing error: {}", finalReport, e);
            return null;
        }
    }

    public void storeReport(final TaskResponseMessage report) {
        String reportKey = getReportKey(report.getToken(), report.getModelName());
//        redisClient.setValue(reportKey, report.getReport(), REPORT_TTL_SEC);
        mongoClient.storeModelReport(report);
    }

    private String getReportKey(final String token, final String modelName) {
        return REPORT_KEY_PREFIX + token + "-" + modelName;
    }
}
