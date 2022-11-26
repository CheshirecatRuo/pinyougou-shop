package com.luoqing.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "alidayu")
@Data
public class Alidayu {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;
}
