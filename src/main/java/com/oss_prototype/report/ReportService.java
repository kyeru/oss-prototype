package com.oss_prototype.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportService {
    private final ObjectMapper jsonMapper;

    public ReportService() {
        this.jsonMapper = new ObjectMapper();
    }

    public String generateReport(final String token) {
        try {
            // 1. access DB with token
            // 2. generate report
            return jsonMapper.writeValueAsString(new DetectionReport());
        } catch (JsonProcessingException e) {
            log.error("json processing error");
        }
        return null;
    }
}
