package com.oss_prototype.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.db_utils.KafkaProducer;
import com.oss_prototype.db_utils.RedisClientWrapper;
import com.oss_prototype.request.TaskRequest;
import com.oss_prototype.models.TaskMessage;
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

    public String processDetectionRequest(final TaskRequest request) {
        log.info("plugin request: {}", request);
        if (request == null) {
            return null;
        }
        try {
            // 1. generate token from request data
            String token = RequestTokenGenerator.generate(request);
            log.info("request token generated: {}", token);

            // 2. deliver the request to model servers via kafka
            sendRequestMessage(token, request.getData());

            // 3. create task status
            updateTaskStatus(token, WORK_IN_PROGRESS);

            // 4. return the token
            return token;
        } catch (JsonProcessingException e) {
            log.error("json processing error: {}", request.toString(), e);
        } catch (Exception e) {
            log.error("token generation failed: ", e);
        }
        return null;
    }

    private void sendRequestMessage(final String token, final String data) throws JsonProcessingException {
        TaskMessage modelTask = TaskMessage.builder()
            .token(token)
            .payload(data)
            .build();
        String jsonTaskMessage = jsonMapper.writeValueAsString(modelTask);
        kafkaProducer.send(jsonTaskMessage);
        log.info("message to models: {}", jsonTaskMessage);
    }

    private void updateTaskStatus(final String token, final String status) {
        redisClient.setValue(WORK_STATUS_KEY_PREFIX + token, status, WORK_STATUS_TTL_SEC);
    }
}
