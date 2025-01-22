package com.oss_prototype.controller;

import com.oss_prototype.service.DetectionService;
import com.oss_prototype.request.DetectionRequest;
import com.oss_prototype.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/detection")
@Slf4j
public class PluginRequestControllerV1 {
    private final DetectionService detectionService;
    private final ReportService reportService;

    public PluginRequestControllerV1(
            final DetectionService detectionService,
            final ReportService reportService) {
        this.detectionService = detectionService;
        this.reportService = reportService;
    }

    @PostMapping("/init")
    public ResponseEntity<?> initDetection(@RequestBody DetectionRequest requestData) {
        String token = detectionService.processDetectionRequest(requestData);
        if (token == null) {
            // TODO return error hint
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @GetMapping("/progress")
    public ResponseEntity<?> checkProgress(@RequestParam String token) {
        String report = reportService.generateReport(token);
        if (report == null) {
            // TODO return error hint
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
    }
}
