package com.oss_prototype.service;

import com.oss_prototype.response.ExecutionHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExecutionHistoryService {
    public ExecutionHistory getHistory(final String user) {
        return new ExecutionHistory();
    }
}
