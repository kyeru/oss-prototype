package com.oss_prototype.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FinalReport {
    public String token;
    List<ModelReportEntry> reports;

    @AllArgsConstructor
    static public class ModelReportEntry {
        public String modelName;
        public String report;
    }
}
