package com.oss_prototype.controller;

import com.oss_prototype.response.ExecutionHistory;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/history")
@Tag(name = "web controller v1")
public class WebRequestControllerV1 {
    @GetMapping("/history")
    public ExecutionHistory history(@RequestParam String user) {
        return new ExecutionHistory();
    }
}
