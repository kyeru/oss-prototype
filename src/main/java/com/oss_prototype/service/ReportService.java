package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.db_utils.RedisClientWrapper;
import com.oss_prototype.models.ModelName;
import com.oss_prototype.response.ModelReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ReportService {
    private static final String REPORT_KEY_PREFIX = "report-";
    private static final long REPORT_TTL_SEC = 86400;

    private final ObjectMapper jsonMapper;
    private final RedisClientWrapper redisClient;

    public ReportService(final RedisClientWrapper redisClient) {
        this.jsonMapper = new ObjectMapper();
        this.redisClient = redisClient;
    }

    public String generateReport(final String token) {
        // 1. check report from each model
        Map<String, String> reports = new HashMap<>();
        for (ModelName name : ModelName.values()) {
            String reportKey = getReportKey(token, name.getName());
            String storedReport = redisClient.getValue(reportKey);
            if (storedReport != null) {
                reports.put(name.getName(), storedReport);
            }
        }

        // 2. combines reports
        try {
            log.warn("final report: {}", reports);
            return jsonMapper.writeValueAsString(reports);
        } catch (JsonProcessingException e) {
            log.error("json processing error");
        }
        return null;
    }

    public void storeReport(final ModelReport report) {
        String reportKey = getReportKey(report.getToken(), report.getModelName());
        redisClient.setValue(reportKey, report.getReport(), REPORT_TTL_SEC);
    }

    private String getReportKey(final String token, final String modelName) {
        return REPORT_KEY_PREFIX + token + "-" + modelName;
    }
}
