package com.oss_prototype.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class TaskResponseMessage {
    @Field
    private String modelName;
    @Field
    private String modelVersion;
    @Field
    private String token;
    @Field
    private String returnCode;
    @Field
    private String errorMessage;
    @Field
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime taskStartTime;
    @Field
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime taskEndTime;
    @Field
    private String report;

    public String getId() {
        return token + '-' + modelName;
    }
}
