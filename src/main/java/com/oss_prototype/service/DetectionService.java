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

import java.io.UnsupportedEncodingException;

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

    public String processDetectionRequest(final DetectionRequest request) {
        log.info("plugin request: {}", request);
        if (request == null) {
            return null;
        }
        try {
            // 1. generate token from request data
            String token = RequestTokenGenerator.generate(request);
            log.info("request token generated: {}", token);

            // 2. send request to model servers via kafka
            sendRequestMessage(token, request.getData());

            // 3. create task status
            updateTaskStatus(token, WORK_IN_PROGRESS);

            return token;
        } catch (JsonProcessingException e) {
            log.error("json processing error: {}", request);
            // TODO return error code/message
        } catch (UnsupportedEncodingException e) {
            log.error("token generation error: {}", request);
        } catch (Exception e) {
            log.error("token generation failed: ", e);
            // TODO return error code/message
        }
        return null;
    }

    private void sendRequestMessage(final String token, final String data) throws JsonProcessingException {
        ModelTaskMessage modelTask = ModelTaskMessage.builder()
            .token(token)
            .requestData(data)
            .build();
        String jsonTaskMessage = jsonMapper.writeValueAsString(modelTask);
        kafkaProducer.send(jsonTaskMessage);
        log.info("message to models: {}", jsonTaskMessage);
    }

    private void updateTaskStatus(final String token, final String status) {
        redisClient.setValue(WORK_STATUS_KEY_PREFIX + token, status, WORK_STATUS_TTL_SEC);
    }
}
