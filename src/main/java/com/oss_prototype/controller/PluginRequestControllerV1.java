package com.oss_prototype.controller;

import com.oss_prototype.service.DetectionService;
import com.oss_prototype.request.TaskRequest;
import com.oss_prototype.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/task")
@Slf4j
@Tag(name = "plugin API v1")
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
    @Operation(summary = "탐지 시작 요청")
    public ResponseEntity<?> initDetection(
            @Parameter(description = "request from plugins") @RequestBody TaskRequest requestData) {
        String token = detectionService.processDetectionRequest(requestData);
        if (token == null) {
            return new ResponseEntity<>("token generation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @GetMapping("/progress")
    @Operation(summary = "탐지 진행 상황과 결과 확인")
    public ResponseEntity<?> checkProgress(@RequestParam String token) {
        String report = reportService.generateReport(token);
        if (report == null) {
            return new ResponseEntity<>("report generation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
    }
}
