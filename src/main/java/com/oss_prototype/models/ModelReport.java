package com.oss_prototype.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModelReport {
    private String token;
    private String modelName;
    private String status;
    private Timestamp taskStartTime;
    private Timestamp taskEndTime;
    private String report;

    public String getId() {
        return token + '-' + modelName;
    }
}
