package com.oss_prototype.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.redis.RedisClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
        try {
            // 1. access DB with token
            String reportKey = getReportKey("", token);
            String report = redisClient.getValue(reportKey);
            // 2. generate report
            return jsonMapper.writeValueAsString(
                Objects.requireNonNullElseGet(report, ModelReport::new));
        } catch (JsonProcessingException e) {
            log.error("json processing error");
        }
        return null;
    }

    public String getReportKey(final String modelName, final String token) {
        return REPORT_KEY_PREFIX + token;
        // TODO make a unique report key
//        return modelName + "-" + token;
    }

    public void storeReport(final String reportKey, final String report) {
        redisClient.setValue(reportKey, report, REPORT_TTL_SEC);
    }
}
