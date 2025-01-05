package com.oss_prototype.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class DetectionRequest {
    private Timestamp timestamp;
    private String user;
    private String data;

    public String toString() {
        return timestamp.toString() + " " + user + " " + data;
    }
}
