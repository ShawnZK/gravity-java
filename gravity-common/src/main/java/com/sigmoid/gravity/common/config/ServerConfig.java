package com.sigmoid.gravity.common.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerConfig {

    private int serverType;

    private int port;

    private int threadCount;

    private int queueSize;

}
