package com.sigmoid.gravity.common.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TracerServerConfig extends ServerConfig {

    private int serverType;

    private int port;

    private int threadCount;

    private int queueSize;

}
