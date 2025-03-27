package com.oss_prototype.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@ToString
public class TaskRequestMessage {
    private Timestamp timestamp;
    private String token;
    private String user;
    // temporary field for test
    private String modelName;
    private String value;
}
