package com.oss_prototype.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModelTaskMessage {
    private String token;
    private String requestData;
}
