package com.oss_prototype.db_utils;

import com.oss_prototype.models.TaskResponseMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<TaskResponseMessage, String> {
    public List<TaskResponseMessage> findByToken(String token);
}
