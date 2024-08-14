package com.neuvector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Rule
    public TemporaryFolder mountFolder= new TemporaryFolder();

    private static final Logger log = LoggerFactory.getLogger(ScannerTest.class);

    @Test
    public void scanLocalImageTest() throws IOException {
        String license = "";

        String imageName = "alpine";
        String imageTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";
        //mountPath is an optional parameter. It will use "/var/neuvector" by default.
        String mountPath = mountFolder.getRoot().getAbsolutePath();

        Image image = new Image(imageName, imageTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath, log, null, "docker", "");

        ScanRepoReportData scanReportData = Scanner.scanLocalImage(image,scanner,license);

        UserPrincipal user = getUserPrincipal(mountPath);

        assertFalse(user.getName().equals("root"));
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
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        //mountPath is an optional parameter. It will use "/var/neuvector" by default.
        String mountPath = mountFolder.getRoot().getAbsolutePath();

        Registry registry = new Registry(registryURL, regUser, regPassword,repository,repositoryTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath, log, null, "docker", "");

        ScanRepoReportData scanReportData = Scanner.scanRegistry(registry, scanner, license);

        UserPrincipal user = getUserPrincipal(mountPath);

        assertFalse(user.getName().equals("root"));
        assertTrue( scanReportData != null );
    }

    @Test
    public void scanLocalImageTest_BindMountShared() throws IOException {
        String license = "";

        String imageName = "alpine";
        String imageTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = null;
        String nvRegistryPassword = null;
        String nvRegistryURL = "https://registry.hub.docker.com";
        boolean bindMountShared = true;
        //mountPath is an optional parameter. It will use "/var/neuvector" by default.
        String mountPath = mountFolder.getRoot().getAbsolutePath();

        Image image = new Image(imageName, imageTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath, log, bindMountShared, "docker", "");

        ScanRepoReportData scanReportData = Scanner.scanLocalImage(image,scanner,license);

        UserPrincipal user = getUserPrincipal(mountPath);

        assertFalse(user.getName().equals("root"));
        assertTrue( scanReportData != null );
    }

    @Test
    public void scanRegistryTest_BindMountShared() throws IOException {
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
        boolean bindMountShared = true;
        //mountPath is an optional parameter. It will use "/var/neuvector" by default.
        String mountPath = mountFolder.getRoot().getAbsolutePath();

        Registry registry = new Registry(registryURL, regUser, regPassword,repository,repositoryTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath, log, bindMountShared, "docker", "");

        ScanRepoReportData scanReportData = Scanner.scanRegistry(registry, scanner, license);

        UserPrincipal user = getUserPrincipal(mountPath);

        assertFalse(user.getName().equals("root"));
        assertTrue( scanReportData != null );
    }

    private static UserPrincipal getUserPrincipal(String mountPath) throws IOException {
        UserPrincipal user = null;
        Path path = Paths.get(mountPath + "/scan_result.json");
        FileOwnerAttributeView fileOwner = Files.getFileAttributeView(path,
                FileOwnerAttributeView.class);
        user = fileOwner.getOwner();
        return user;
    }
}
