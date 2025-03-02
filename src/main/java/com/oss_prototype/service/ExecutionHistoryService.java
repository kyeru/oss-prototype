package com.oss_prototype.service;

import com.oss_prototype.response.ExecutionHistory;
import com.oss_prototype.response.ExecutionHistory.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExecutionHistoryService {
    public ExecutionHistory getHistory(final String user) {
        return new ExecutionHistory();
    }

    private List<TaskResult> fetchTaskResults(final String token) {
        List<TaskResult> taskResults = new ArrayList<>();

        return taskResults;
    }
}
