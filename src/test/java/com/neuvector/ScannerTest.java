package com.neuvector;

import static org.junit.Assert.assertTrue;

import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;

/**
 * Unit tests
 */
public class ScannerTest 
{
    @Test
    public void scanLocalImageTest() throws IOException {
        String license = "";

        String imageName = "alpine";
        String imageTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = "";
        String nvRegistryPassword = "";
        String nvRegistryURL = "https://registry.hub.docker.com";
        String mountPath = "/temp";  //mountPath is an optional parameter. It will use "/var/neuvector" by default.

        Image image = new Image(imageName, imageTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath);

        ScanRepoReportData scanReportData = Scanner.scanLocalImage(image,scanner,license);

        File file = new File(mountPath + "/scan_result.json");
        UserPrincipal user = getUserPrincipal(mountPath, file);

        assertTrue(user.getName().equals("root") || user.getName().contains(System.getProperty("user.name")));
        assertTrue( scanReportData != null );
    }

    @Test
    public void scanRegistryTest() throws IOException {
        String license = "";

        String registryURL = "https://registry.hub.docker.com";
        String regUser = "";
        String regPassword = "";
        String repository = "library/alpine";
        String repositoryTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryURL = registryURL;
        String nvRegistryUser = "";
        String nvRegistryPassword = "";
        String mountPath = "/temp";  //mountPath is an optional parameter. It will use "/var/neuvector" by default.

        Registry registry = new Registry(registryURL, regUser, regPassword,repository,repositoryTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath);

        ScanRepoReportData scanReportData = Scanner.scanRegistry(registry, scanner, license);

        File file = new File(mountPath + "/scan_result.json");
        UserPrincipal user = getUserPrincipal(mountPath, file);

        assertTrue(user.getName().equals("root") || user.getName().contains(System.getProperty("user.name")));
        assertTrue( scanReportData != null );
    }

    private static UserPrincipal getUserPrincipal(String mountPath, File file) throws IOException {
        UserPrincipal user = null;
        if (file.exists()) {
            Path path = Paths.get(mountPath + "/scan_result.json");
            FileOwnerAttributeView fileOwner = Files.getFileAttributeView(path,
                    FileOwnerAttributeView.class);
            user = fileOwner.getOwner();
        }
        return user;
    }
}
