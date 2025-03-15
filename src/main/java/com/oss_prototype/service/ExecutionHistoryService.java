package com.oss_prototype.service;

import com.oss_prototype.db_utils.MongoClient;
import com.oss_prototype.models.ModelReport;
import com.oss_prototype.response.ExecutionHistory;
import com.oss_prototype.response.ExecutionHistory.TaskResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExecutionHistoryService {
    @Autowired
    private MongoClient mongoClient;

    public ExecutionHistory getHistory(final String user) {
        List<TaskResult> taskResults = fetchTaskResults();

        return ExecutionHistory.builder()
            .taskResults(taskResults)
            .build();
    }

    private List<TaskResult> fetchTaskResults() {
        List<TaskResult> taskResults = new ArrayList<>();
        List<ModelReport> reports = mongoClient.fetchAllModelReports(10);
        reports.stream().forEach(
            report -> taskResults.add(TaskResult.builder()
                .modelName(report.getModelName())
//                .status(report.getStatus())
                .timeConsumed(
                    Duration.between(
                        Optional.ofNullable(report.getTaskStartTime()).orElse(LocalDateTime.MIN),
                        Optional.ofNullable(report.getTaskEndTime()).orElse(LocalDateTime.MIN)
                    ).toMillis()
                )
                .build())
        );
        return taskResults;
    }
}
