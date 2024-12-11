package com.oss_prototype.detection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oss_prototype.kafka.KafkaProducer;
import com.oss_prototype.models.ModelName;
import com.oss_prototype.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
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

    public String detectionWorkflow(final PluginRequest request) {
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
                .payload(request.getData())
                .build();
            String jsonTaskMessage = jsonMapper.writeValueAsString(taskMessage);
            kafkaProducer.sendMessage(jsonTaskMessage);
            log.info("message to models: {}", jsonTaskMessage);

            // 3. create job status
            redisClient.setValue(token, WORK_IN_PROGRESS);

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

    public String getReportKey(final String modelName) {
        // TODO make a unique report key
        return modelName;
    }

    public void updateTaskStatus(final String token, final String status) {
        redisClient.setValue(token, WORK_COMPLETE);
    }
}
