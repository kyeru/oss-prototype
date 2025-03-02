package com.oss_prototype.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class ExecutionHistory {
    private Timestamp timestamp;
    private String token;

    @Getter
    @Setter
    @Builder
    @ToString
    public static class TaskResult {
        private String modelName;
        private String status;
        private long timeConsumed;    // in msec
    }
}
