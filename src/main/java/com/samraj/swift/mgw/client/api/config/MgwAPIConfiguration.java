package com.samraj.swift.mgw.client.api.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mgw.api")
@Data
@ToString
public class MgwAPIConfiguration {
    private String appName;
    private String sharedKey;
    private String profileId;
    private String url;
    private String headerSignatureAdd;
}
