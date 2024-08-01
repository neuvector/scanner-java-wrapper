package com.neuvector;

import com.neuvector.model.NVScanner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class NvScannerTest {

    private static final Logger log = LoggerFactory.getLogger(ScannerTest.class);

    @Rule
    public TemporaryFolder mountFolder= new TemporaryFolder();

    @Test
    public void defaultAsDockerTest() {

        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";
        String nvMountPath = mountFolder.getRoot().getAbsolutePath();

        NVScanner nvs = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, nvMountPath, log,null);

        assertEquals(NVScanner.DEFAULT_CONTAINER_RUNTIME_COMMAND, nvs.getContainerRuntimeCommand());
        assertEquals(NVScanner.DEFAULT_CONTAINER_RUNTIME_SOCKET, nvs.getContainerRuntimeSocket());
    }

    @Test
    public void commandAsPodmanTest() {

        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";
        String nvMountPath = mountFolder.getRoot().getAbsolutePath();

        NVScanner nvs = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, nvMountPath, log,null, "podman");

        assertEquals("podman", nvs.getContainerRuntimeCommand());
        assertEquals(NVScanner.CONTAINER_RUNTIME_SOCKET_PODMAN, nvs.getContainerRuntimeSocket());
    }

    @Test
    public void customPodmanTest() {

        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";
        String nvMountPath = mountFolder.getRoot().getAbsolutePath();
        String nvContainerCommand = "podman";
        String nvContainerSocketPath = "/my/path.sock";

        NVScanner nvs = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, nvMountPath, log,null, nvContainerCommand, nvContainerSocketPath);

        assertEquals(nvContainerCommand, nvs.getContainerRuntimeCommand());
        assertNotEquals(NVScanner.CONTAINER_RUNTIME_SOCKET_PODMAN, nvs.getContainerRuntimeSocket());
        assertEquals(nvContainerSocketPath, nvs.getContainerRuntimeSocket());
    }

    @Test
    public void customEverythingTest() {

        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";
        String nvMountPath = mountFolder.getRoot().getAbsolutePath();
        String nvContainerCommand = "containerd";
        String nvContainerSocketPath = "/my/to/path.sock";

        NVScanner nvs = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, nvMountPath, log,null, nvContainerCommand, nvContainerSocketPath);

        assertEquals(nvContainerCommand, nvs.getContainerRuntimeCommand());
        assertNotEquals(NVScanner.CONTAINER_RUNTIME_SOCKET_PODMAN, nvs.getContainerRuntimeSocket());
        assertEquals(nvContainerSocketPath, nvs.getContainerRuntimeSocket());
    }
}
