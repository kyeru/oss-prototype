package com.oss_prototype.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Builder
public class DetectionRequest {
    private Timestamp timestamp;
    private String user;
    // private String clientAddr;
    private String data;
}
