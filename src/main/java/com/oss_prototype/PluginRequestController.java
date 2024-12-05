package com.oss_prototype;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.detection.DetectionService;
import com.oss_prototype.kafka.KafkaProducer;
import com.oss_prototype.redis.RedisClient;
import com.oss_prototype.detection.DetectionRequest;
import com.oss_prototype.detection.RequestTokenGenerator;
import com.oss_prototype.report.DetectionReport;
import com.oss_prototype.report.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class PluginRequestController {
    private final DetectionService detectionService;
    private final ReportService reportService;

    public PluginRequestController(
            final DetectionService detectionService,
            final ReportService reportService) {
        this.detectionService = detectionService;
        this.reportService = reportService;
    }

    @PostMapping("/detect")
    public ResponseEntity<?> detect(@RequestBody DetectionRequest requestData) {
        String token = detectionService.detectionWorkflow(requestData);
        if (token == null) {
            // TODO return error hint
            return new ResponseEntity<>(new String(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @GetMapping("/report")
    public ResponseEntity<?> report(@RequestParam String token) {
        String report = reportService.generateReport(token);
        if (report == null) {
            // TODO return error hint
            return new ResponseEntity<>(new String(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
    }
}
