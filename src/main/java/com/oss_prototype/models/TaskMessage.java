package com.oss_prototype.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TaskMessage {
    private String token;
    private String payload;
}
