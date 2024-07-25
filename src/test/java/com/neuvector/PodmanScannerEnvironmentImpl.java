package com.neuvector;

public class PodmanScannerEnvironmentImpl implements ScannerEnvironment {
    @Override
    public void create() {}

    @Override
    public String GetRuntimeSocket() {
        return "/var/run/podman.sock";
    }
}
