package com.oss_prototype.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncReportChecker {
    @KafkaListener(topics="${spring.kafka.report-topic}")
    public void generateReport(final String content) {
        // 1. identify report(system) type
        // 2. store the report with a proper key
        // 3. update task status
        log.info("report: {}", content);
    }
}
