package com.oss_prototype.service;

import com.oss_prototype.db_utils.MongoClientWrapper;
import com.oss_prototype.models.TaskResponseMessage;
import com.oss_prototype.response.ExecutionHistory;
import com.oss_prototype.response.ExecutionHistory.TaskResult;
import com.oss_prototype.response.RequestHistory;
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
    private MongoClientWrapper mongoClientWrapper;

    public ExecutionHistory getHistory(final String user) {
        RequestHistory requestHistory = mongoClientWrapper.fetchRequestHistory(user);
        if (requestHistory == null) {
            return null;
        }
        List<TaskResult> taskResults = fetchTaskResults();

        return ExecutionHistory.builder()
            .timestamp(Timestamp.valueOf(requestHistory.getRequestTime()))
            .token(requestHistory.getToken())
            .user(requestHistory.getUser())
            .taskType(requestHistory.getTaskType())
            .taskResults(taskResults)
            .build();
    }

    private List<TaskResult> fetchTaskResults() {
        List<TaskResult> taskResults = new ArrayList<>();
        List<TaskResponseMessage> responses = mongoClientWrapper.fetchAllModelReports(10);
        responses.stream().forEach(
            response -> taskResults.add(TaskResult.builder()
                .modelName(response.getModelName())
                .returnCode(response.getReturnCode())
                .errorMessage(response.getErrorMessage())
                .taskStartTime(Timestamp.valueOf(response.getTaskStartTime()))
                .taskEndTime(Timestamp.valueOf(response.getTaskEndTime()))
                .timeConsumed(
                    Duration.between(
                        Optional.ofNullable(response.getTaskStartTime()).orElse(LocalDateTime.MIN),
                        Optional.ofNullable(response.getTaskEndTime()).orElse(LocalDateTime.MIN)
                    ).toMillis()
                )
                .build())
        );
        return taskResults;
    }
}
