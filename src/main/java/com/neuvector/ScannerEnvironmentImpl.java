package com.neuvector;

public class ScannerEnvironmentImpl implements ScannerEnvironment {

    private String containerSocket;

    @Override
    public void create() {
        String containerRuntimeSocket = System.getenv(ScannerEnvironment.ENV_CONTAINER_RUNTIME_SOCKET);

        if (containerRuntimeSocket == null) {
            containerRuntimeSocket = ScannerEnvironment.DEFAULT_RUNTIME_SOCKET_SOURCE;
        }

        this.containerSocket = containerRuntimeSocket;
    }

    @Override
    public String GetRuntimeSocket() {
        return this.containerSocket;
    }
}
