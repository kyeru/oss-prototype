package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.models.TaskResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncModelReportReceiver {
    private final ObjectMapper jsonMapper;
//    private ModelTaskService modelTaskService;
    private ReportService reportService;

    public AsyncModelReportReceiver(ModelTaskService modelTaskService, ReportService reportService) {
//        this.modelTaskService = modelTaskService;
        this.reportService = reportService;
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    }

    @KafkaListener(topics="${spring.kafka.consumer.topic}")
    public void storeReport(final String message) {
        TaskResponseMessage taskResponseMessage = parseModelResponse(message);
        if (taskResponseMessage == null) {
            log.warn("model response is null");
            return;
        }

        log.info("model response: {}", taskResponseMessage);
        reportService.storeReport(taskResponseMessage);
    }

    private TaskResponseMessage parseModelResponse(final String message) {
        try {
            // double decoding: kafka message -> json -> object
            String decodedJson = jsonMapper.readValue(message, String.class);
            return jsonMapper.readValue(decodedJson, TaskResponseMessage.class);
        } catch (Exception e) {
            log.warn("model response parsing error: {}", message, e);
            return null;
        }
    }
}
