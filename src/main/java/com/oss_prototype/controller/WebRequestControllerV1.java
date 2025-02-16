package com.oss_prototype.controller;

import com.oss_prototype.response.ExecutionHistory;
import com.oss_prototype.service.ExecutionHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/history")
@Tag(name = "web controller v1")
public class WebRequestControllerV1 {
    @Autowired
    private ExecutionHistoryService executionHistoryService;

    @GetMapping("/history")
    public ResponseEntity<?> history(@RequestParam String user) {
        ExecutionHistory history = executionHistoryService.getHistory(user);
        if (history == null) {
            return new ResponseEntity<>("history listing failed", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
    }
}
