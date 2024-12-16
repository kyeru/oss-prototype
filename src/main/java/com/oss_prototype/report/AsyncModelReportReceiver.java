package com.oss_prototype.report;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.detection.DetectionService;
import com.oss_prototype.models.ModelName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncModelReportReceiver {
    private final ObjectMapper jsonMapper;
    private DetectionService detectionService;

    public AsyncModelReportReceiver(DetectionService detectionService) {
        this.detectionService = detectionService;
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
    }

    @KafkaListener(
        topics="${spring.kafka.report-topic}"
    )
    public void generateReport(final String content) {
        ModelReport modelReport = generateModelReport(content);
        if (modelReport == null) {
            return;
        }
        log.info("generated model report: {}", modelReport);

        // 1. identify model name and request token
        String modelName = modelReport.getModelName();
        String token = modelReport.getToken();

        // 2. store the report with a dedicated key
        String reportKey = detectionService.getReportKey(modelName);
        // TODO store report with the key

        // 3. update task status
        detectionService.updateTaskStatus(token, "");
    }

    public ModelReport generateModelReport(final String report) {
        try {
            String decodedJson = jsonMapper.readValue(report, String.class);
            log.info("report string: {}", decodedJson);
            return jsonMapper.readValue(decodedJson, ModelReport.class);
//            String r = "\n    {\n        \"modelName\": \"mock\",\n        \"token\": \"OOJsdwM/Dc70yn3IRESf6EocdYoFW1/JMxyCZw2iA/g=\",\n        \"report\": \"report\"\n    }\n    ";
//            log.info("cloned report: {}", r);
//            return jsonMapper.readValue(r, ModelReport.class);
        } catch (Exception e) {
            log.warn("report parsing error: {}", report, e);
            return null;
        }
    }
}
