package com.oss_prototype.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TaskRequestMessage {
    private String token;
    private String payload;
}
