package com.neuvector;

/**
 * Abstract the environment variables to allow for testing.
 */
public interface ScannerEnvironment {

    public static final String ENV_CONTAINER_RUNTIME_SOCKET = "CONTAINER_RUNTIME_SOCKET";

    public static final String DEFAULT_RUNTIME_SOCKET_SOURCE = "/var/run/docker.sock";

    public void create();

    public String GetRuntimeSocket();
}


