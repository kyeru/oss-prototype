package com.oss_prototype.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ExecutionHistory {
    private Timestamp timestamp;
    private String token;
    private String status;
    private String value;
}
