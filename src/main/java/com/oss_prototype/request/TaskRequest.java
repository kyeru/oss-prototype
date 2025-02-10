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
    private Timestamp timestamp;

    @Schema(example = "{ json map (will be defined later) }")
    private String metadata;

    @Schema(example = "task payload here")
    private String data;
}
