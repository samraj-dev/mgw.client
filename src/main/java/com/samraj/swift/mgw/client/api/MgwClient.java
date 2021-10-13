package com.samraj.swift.mgw.client.api;

import com.samraj.swift.mgw.client.api.config.MgwAPIConfiguration;
import com.samraj.swift.mgw.client.util.Util;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;

@Service
@Slf4j
public class MgwClient {

    @Autowired(required = false)
    private HostnameVerifier verifier;

    @Autowired
    private MgwAPIConfiguration configuration;

    @Value("${mgw.server.tls.truststore.location}")
    private String trustStoreLocation;

    @Value("${mgw.server.tls.truststore.password}")
    private String trustStorePassword;


    private OkHttpClient client = null;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (null != verifier) {
            log.info("Using hostname verification {}", verifier.getClass().getName());
            builder.hostnameVerifier(verifier);
        }

        //Setting up the trust store to connect to microgateway
        log.trace("Truststore location: {}", trustStoreLocation);
        log.trace("Truststore password: {}", trustStorePassword);
        if (null != trustStoreLocation && null != trustStorePassword) {
            System.setProperty("javax.net.ssl.trustStore", trustStoreLocation);
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
            log.debug("Using truststore location {}, and password {}", trustStoreLocation, trustStorePassword);
        } else {
            log.warn("Truststore and password not set explicitly");
        }
        client = builder.build();
        log.info("Microgateway Configuration loaded {}", configuration);
    }

    /**
     *
     * @return
     */
    public String callGetApi(String dUrl) {
        try {
            /* Implementation for V4 changed transactions. */
            String url = configuration.getUrl();
            if (null != dUrl && !dUrl.isEmpty()) {
                url = dUrl;
            }
            log.info("URL: {}", url);
            String jwsSign = null;

            String payload = Util.buildPayload(configuration.getAppName(), configuration.getProfileId(), url, null);
            /* Sign the request. */
            jwsSign = Util.sign(payload, configuration.getSharedKey());
            log.info("JWS Sign: {}", jwsSign);
            /* Add authorization header to the request. */
            Request.Builder builder = new Request.Builder().url(url).addHeader("Authorization", "Bearer " + jwsSign);
            if ("true".equals(configuration.getHeaderSignatureAdd())) {
                builder.addHeader("X-SWIFT-Signature", "false");
            }
            Request request = builder.build();

            Call call = client.newCall(request);
            Response response = call.execute();

            String jwsToken = response.header("Authorization");
            log.info("JWS Token: {}", jwsToken);
            String responseString = response.body().string();

            if (Util.verifyResponse(responseString, jwsToken, configuration.getSharedKey())) {
                log.info("Response JWS signature is successfully verified");
            } else {
                log.warn("Failed to verify response JWS signature.");
            }

            log.info("Response {}", responseString);
            return responseString;
        } catch (Exception ex) {
            log.error("Error while running callGetApi", ex);
        }
        return null;
    }
}
