package com.oss_prototype.response;

import com.oss_prototype.models.ModelName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelReport {
//    private ModelName modelName;
    private String modelName;
//    private String jobStatus;
    private String token;
    private String report;
}
