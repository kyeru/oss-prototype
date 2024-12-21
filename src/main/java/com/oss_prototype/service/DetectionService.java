package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.db_utils.KafkaProducer;
import com.oss_prototype.db_utils.RedisClientWrapper;
import com.oss_prototype.request.DetectionRequest;
import com.oss_prototype.request.ModelTaskMessage;
import com.oss_prototype.request.RequestTokenGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DetectionService {
    public static final String WORK_STATUS_KEY_PREFIX = "status-";
    public static final String WORK_IN_PROGRESS = "in-progress";
    public static final String WORK_COMPLETE = "complete";
    private static final long WORK_STATUS_TTL_SEC = 3600;

    private final ObjectMapper jsonMapper;
    private final KafkaProducer kafkaProducer;
    private final RedisClientWrapper redisClient;

    public DetectionService(final KafkaProducer kafkaProducer, final RedisClientWrapper redisClient) {
        this.jsonMapper = new ObjectMapper();
        this.kafkaProducer = kafkaProducer;
        this.redisClient = redisClient;
    }

    public String detectionWorkflow(final DetectionRequest request) {
        log.info("plugin request: {}", request);
        if (request.getData() == null) {
            return null;
        }
        try {
            // 1. generate token from request data
            String token = RequestTokenGenerator.generate(request.getData());

            // 2. send request to kafka
            ModelTaskMessage taskMessage = ModelTaskMessage.builder()
                .token(token)
                .requestData(request.getData())
                .build();
            String jsonTaskMessage = jsonMapper.writeValueAsString(taskMessage);
            kafkaProducer.sendMessage(jsonTaskMessage);
            log.info("message to models: {}", jsonTaskMessage);

            // 3. create job status
            updateTaskStatus(token, WORK_IN_PROGRESS);

            // 4. return token
            return token;
        } catch (JsonProcessingException e) {
            log.error("json processing error: {}", request);
            // TODO return error code/message
        } catch (Exception e) {
            log.error("token generation failed: ", e);
            // TODO return error code/message
        }
        return null;
    }

    public void updateTaskStatus(final String token, final String status) {
        redisClient.setValue(WORK_STATUS_KEY_PREFIX + token, status, WORK_STATUS_TTL_SEC);
    }
}
