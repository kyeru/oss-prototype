package com.oss_prototype.controller;

import com.oss_prototype.service.ModelTaskService;
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
    private final ModelTaskService modelTaskService;
    private final ReportService reportService;

    public PluginRequestControllerV1(
            final ModelTaskService modelTaskService,
            final ReportService reportService) {
        this.modelTaskService = modelTaskService;
        this.reportService = reportService;
    }

    @PostMapping("/init")
    @Operation(summary = "작업 요청")
    public ResponseEntity<?> initDetection(
            @Parameter(description = "request from plugins") @RequestBody TaskRequest requestData) {
        String token = modelTaskService.processTaskRequest(requestData);
        if (token == null) {
            return new ResponseEntity<>("token generation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
    }

    @GetMapping("/progress")
    @Operation(summary = "실행 결과 확인")
    public ResponseEntity<?> checkProgress(@RequestParam String token) {
        String report = reportService.generateReport(token);
        if (report == null) {
            return new ResponseEntity<>("report generation failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(report, HttpStatus.OK);
        }
    }
}
