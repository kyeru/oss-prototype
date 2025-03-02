package com.oss_prototype.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TaskMessage {
    private String token;
    private String payload;
}
