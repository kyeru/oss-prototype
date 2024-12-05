package com.oss_prototype.detection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.kafka.KafkaProducer;
import com.oss_prototype.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DetectionService {
    private final ObjectMapper jsonMapper;
    private final KafkaProducer kafkaProducer;
    private final RedisClient redisClient;

    public static final String WORK_IN_PROGRESS = "in-progress";
    public static final String WORK_COMPLETE = "complete";

    public DetectionService(final KafkaProducer kafkaProducer, final RedisClient redisClient) {
        this.jsonMapper = new ObjectMapper();
        this.kafkaProducer = kafkaProducer;
        this.redisClient = redisClient;
    }

    public String detectionWorkflow(final DetectionRequest request) {
        log.info("request data: {}", request);
        try {
            // 1. generate token from request data
            String token = RequestTokenGenerator.generate(request.getPackageMetadata());

            // 2. send request to kafka
            String msg = jsonMapper.writeValueAsString(request);
            kafkaProducer.sendMessage(msg);

            // 3. create job status
            redisClient.setValue(token, WORK_IN_PROGRESS);

            // 4. return token
            return token;
        } catch (JsonProcessingException e) {
            log.error("json processing error: {}", request);
        } catch (Exception e) {
            log.error("token generation failed: ", e);
        }
        return null;
    }
}
