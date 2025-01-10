package com.oss_prototype.db_utils;

import com.oss_prototype.response.ModelReport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<ModelReport, String> {
    public ModelReport findByToken(String token);
}
