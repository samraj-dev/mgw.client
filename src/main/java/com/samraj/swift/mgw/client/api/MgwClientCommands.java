package com.samraj.swift.mgw.client.api;

import com.samraj.swift.mgw.client.api.config.MgwAPIConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@Slf4j
public class MgwClientCommands {

    @Autowired
    private MgwClient mgwClient;

    @Autowired
    private MgwAPIConfiguration mgwAPIConfiguration;

    @ShellMethod(value = "Call SWIFT APIs through Microgateway", key = {"mgw-client", "mgw", "mgwc"})
    public String mgwClient(@ShellOption(defaultValue = "") String dUrl) {
        log.info("URL provided: {}", dUrl);
        return mgwClient.callGetApi(dUrl);
    }
}