package com.oss_prototype.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
public class TaskRequest {
    @Schema(example = "1")
    private String version;
    @Schema
    private Timestamp timestamp;
    @Schema
    private Metadata metadata;
    @Schema(example = "task payload here")
    private String data;

    @Getter
    @Setter
    @ToString
    public static class Metadata {
        @Schema(example = "my-user")
        private String user;
        @Schema(example = "vulnerability")
        private String taskType;
    }
}
