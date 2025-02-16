package com.oss_prototype.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelReport {
    private String token;
    private String modelName;
    private Timestamp taskStartTime;
    private Timestamp taskEndTime;
    private String report;

    public String getId() {
        return token + '-' + modelName;
    }
}
