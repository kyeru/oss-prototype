package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.models.ModelReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncModelReportReceiver {
    private final ObjectMapper jsonMapper;
    private ModelTaskService modelTaskService;
    private ReportService reportService;

    public AsyncModelReportReceiver(ModelTaskService modelTaskService, ReportService reportService) {
        this.modelTaskService = modelTaskService;
        this.reportService = reportService;
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    }

    @KafkaListener(topics="${spring.kafka.consumer.topic}")
    public void storeReport(final String message) {
        ModelReport modelReport = parseModelReport(message);
        if (modelReport == null) {
            return;
        }

        log.info("model report: {}", modelReport);
        reportService.storeReport(modelReport);
    }

    private ModelReport parseModelReport(final String message) {
        try {
            // double escaping from kafka message
            String decodedJson = jsonMapper.readValue(message, String.class);
            return jsonMapper.readValue(decodedJson, ModelReport.class);
        } catch (Exception e) {
            log.warn("report parsing error: {}", message, e);
            return null;
        }
    }
}
