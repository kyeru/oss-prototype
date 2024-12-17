package com.oss_prototype.report;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.detection.DetectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncModelReportReceiver {
    private final ObjectMapper jsonMapper;
    private DetectionService detectionService;
    private ReportService reportService;

    public AsyncModelReportReceiver(DetectionService detectionService, ReportService reportService) {
        this.detectionService = detectionService;
        this.reportService = reportService;
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    }

    @KafkaListener(
        topics="${spring.kafka.consumer.topic}"
    )
    public void generateReport(final String content) {
        ModelReport modelReport = parseModelReport(content);
        if (modelReport == null) {
            return;
        }
        log.info("generated model report: {}", modelReport);

        // 1. identify model name and request token
        String modelName = modelReport.getModelName();
        String token = modelReport.getToken();

        // 2. store the report with a dedicated key
        String reportKey = reportService.getReportKey(modelName, token);
        // TODO store report with the key
        reportService.storeReport(reportKey, modelReport.getReport());

        // 3. update task status
        detectionService.updateTaskStatus(token, DetectionService.WORK_COMPLETE);
    }

    public ModelReport parseModelReport(final String report) {
        try {
            String decodedJson = jsonMapper.readValue(report, String.class);
            log.info("report string: {}", decodedJson);
            return jsonMapper.readValue(decodedJson, ModelReport.class);
        } catch (Exception e) {
            log.warn("report parsing error: {}", report, e);
            return null;
        }
    }
}
