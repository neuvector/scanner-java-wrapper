package com.neuvector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests
 */
public class ScannerTest 
{
    private static final Logger log = LoggerFactory.getLogger(ScannerTest.class);

    @Test
    public void scanLocalImageTest() {
        String license = "";

        String imageName = "alpine";
        String imageTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";

        Image image = new Image(imageName, imageTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, log, "docker", "");

        ScanRepoReportData scanReportData = Scanner.scanLocalImage(image,scanner,license);

        assertTrue( scanReportData != null );
        assertEquals("", scanReportData.getError_message());
        assertTrue(scanReportData.getReport().getBase_os().startsWith("alpine"));
    }

    @Test
    public void scanRegistryTest() {
        String license = "";

        String registryURL = "https://registry.hub.docker.com";
        String regUser = "";
        String regPassword = "";
        String repository = "library/alpine";
        String repositoryTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryURL = registryURL;
        String nvRegistryUser = null;
        String nvRegistryPassword = null;

        Registry registry = new Registry(registryURL, regUser, regPassword,repository,repositoryTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, log, "docker", "");

        ScanRepoReportData scanReportData = Scanner.scanRegistry(registry, scanner, license);

        assertTrue( scanReportData != null );
        assertEquals("", scanReportData.getError_message());
        assertTrue(scanReportData.getReport().getBase_os().startsWith("alpine"));
    }
}
