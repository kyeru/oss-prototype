package com.oss_prototype.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
public class RequestHistory {
    private String version;
//    private Timestamp timestamp;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime requestTime;
    private String token;
    private String user;
    private String taskType;

    public String getId() {
        return user + '-' + token;
    }
}
