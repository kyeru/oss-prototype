package com.oss_prototype.controller;

import com.oss_prototype.response.ExecutionHistory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/history")
public class WebRequestController {
    @GetMapping("/")
    public String index() {
        return "OSS prototype index";
    }

    @GetMapping("/history")
    public ExecutionHistory history() {
        return new ExecutionHistory();
    }
}
