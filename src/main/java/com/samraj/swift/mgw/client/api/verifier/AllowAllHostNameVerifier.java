package com.samraj.swift.mgw.client.api.verifier;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Setting up custom host name verifier to ignore the host name in the certificate provided by MGW.
 * This is only applicable for testing environments and should not be used for Live.
 */
@Component
@ConditionalOnProperty(prefix = "mgw.client.tls", name = "hostname.verification.ignore", havingValue = "true")
public class AllowAllHostNameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        return true;
    }
}
