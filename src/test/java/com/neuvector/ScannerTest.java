package com.neuvector;

import static org.junit.Assert.assertTrue;

import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
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
        String license = "r+wzvemF4mHMIBkvKHXTuuLzw4ML/RW2ARfbC06Qem1VdQ8DXcMWNKbd8GD0uYiLoS9d9XPM8bff4ag66fqwdOa42oNXk9dzP1TTRg0zvVKJNMxbxKAuGMY24x0NlXstc3Jl2BcXq7aTHrlQwzJ/7qD8157+YfcjKJbywbt3WvvdpuoJtJIuKaO9hOfGu9/zbRZznEtZiJj56xaRMVZ/ZkPHIMseRWML7FUr1oft20PYU7ltf1BGPT/LlocAXAJucBpUz5iosRzS1iYJhU4M1kdBhEwQHl6FZXrtzJGYpM21587FyYrkvoIPQNlIijaAOiCXR060PJNfcQzOzEpYRidhVPnHIHQuIn8gcYuU3XwPB9Pq3tGzERpr0aB64EjSSanpcIh/XEhnXijiRP6CfxsHx8RvD/jkXy5VlJpTVFh1Iu7ywE6fePHb3hIe16uCtdYB8fu2p8ksCcnxXbjBUkKvdLLxq5gvOCfCJxknd4GdIS3UJySNblgmlfU6liYcgt555TitN5DAjuMhTaoxj9FoPPwJsl4rrf2+32cqUFI+9+UdNsSd2gWtS/8T2ifPHNVMA7fdPdIWx+/4jXP8u484cLqGG9RutNGPo0k7cmxmgx29FvVOMvpkotD8IZsbCfl8sFHtFRP7PegWYPghZf6yUkw5m4SD+bacVZalnLizZKbDgFuzPmH1LgrroVN1Ngf06JVfO6lDodbAOactsJC7P8DOPRUoOtnCf3ffgshPh+QSTKDOmNvFU51VB7MoLlZiKNaVP6yMqS1bUwxRpI5eehqhqCHy46PWfRHF2EnbiQOyYIjJSY5jE54TzOaKZ613GgdIaMN+mbXg+UWajDevwCvKQo+CrwpBoX72tP/as9G3zIJILaKuXbQRvTtZbgGREMSoLIDxPBYe3FVqMHFg/jUE9iPbsF16JHv/z3EzcAYKwYSyDEyVppTvLjQY0t/Vul4SEj+QWpyW7elAUmatzHdAcX2SB3oDOhUtDyIxw8rtenD9hCOfspJtEFknhtM+Zm1WuoQYRM5/paEVvbHZnu2XHY2cnBtxJZAD8wKktnSPGg3foCXQpQcSEyhfWhVT14WX2/GxgbdwIuxSBcoHlcduca5tKtlKAhJ4kjPPZeQc5A18z48pLRU=";

        String imageName = "songlongtj/alpine";
        String imageTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = "xxx";
        String nvRegistryPassword = "xxx";
        String nvRegistryURL = "https://registry.hub.docker.com";
        String mountPath = "/temp";  //mountPath is an optional parameter. It will use "/var/neuvector" by default.
        File file = new File(mountPath + "/scan_result.json");
        Path path = Paths.get(mountPath + "/scan_result.json");
        FileOwnerAttributeView fileOwner = Files.getFileAttributeView(path,
                FileOwnerAttributeView.class);
        UserPrincipal user = fileOwner.getOwner();
        InetAddress addr = InetAddress.getLocalHost();;
        String hostname = addr.getHostName();

        Image image = new Image(imageName, imageTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath);

        ScanRepoReportData scanReportData = Scanner.scanLocalImage(image,scanner,license);

        assertTrue( !hostname.contains("root" ));
        assertTrue( hostname.contains(user.getName()) );
        assertTrue( file.exists() );
        assertTrue( file.canWrite() );
        assertTrue( file.canRead() );
        assertTrue( scanReportData != null );
    }

    @Test
    public void scanRegistryTest() throws IOException {
        String license = "r+wzvemF4mHMIBkvKHXTuuLzw4ML/RW2ARfbC06Qem1VdQ8DXcMWNKbd8GD0uYiLoS9d9XPM8bff4ag66fqwdOa42oNXk9dzP1TTRg0zvVKJNMxbxKAuGMY24x0NlXstc3Jl2BcXq7aTHrlQwzJ/7qD8157+YfcjKJbywbt3WvvdpuoJtJIuKaO9hOfGu9/zbRZznEtZiJj56xaRMVZ/ZkPHIMseRWML7FUr1oft20PYU7ltf1BGPT/LlocAXAJucBpUz5iosRzS1iYJhU4M1kdBhEwQHl6FZXrtzJGYpM21587FyYrkvoIPQNlIijaAOiCXR060PJNfcQzOzEpYRidhVPnHIHQuIn8gcYuU3XwPB9Pq3tGzERpr0aB64EjSSanpcIh/XEhnXijiRP6CfxsHx8RvD/jkXy5VlJpTVFh1Iu7ywE6fePHb3hIe16uCtdYB8fu2p8ksCcnxXbjBUkKvdLLxq5gvOCfCJxknd4GdIS3UJySNblgmlfU6liYcgt555TitN5DAjuMhTaoxj9FoPPwJsl4rrf2+32cqUFI+9+UdNsSd2gWtS/8T2ifPHNVMA7fdPdIWx+/4jXP8u484cLqGG9RutNGPo0k7cmxmgx29FvVOMvpkotD8IZsbCfl8sFHtFRP7PegWYPghZf6yUkw5m4SD+bacVZalnLizZKbDgFuzPmH1LgrroVN1Ngf06JVfO6lDodbAOactsJC7P8DOPRUoOtnCf3ffgshPh+QSTKDOmNvFU51VB7MoLlZiKNaVP6yMqS1bUwxRpI5eehqhqCHy46PWfRHF2EnbiQOyYIjJSY5jE54TzOaKZ613GgdIaMN+mbXg+UWajDevwCvKQo+CrwpBoX72tP/as9G3zIJILaKuXbQRvTtZbgGREMSoLIDxPBYe3FVqMHFg/jUE9iPbsF16JHv/z3EzcAYKwYSyDEyVppTvLjQY0t/Vul4SEj+QWpyW7elAUmatzHdAcX2SB3oDOhUtDyIxw8rtenD9hCOfspJtEFknhtM+Zm1WuoQYRM5/paEVvbHZnu2XHY2cnBtxJZAD8wKktnSPGg3foCXQpQcSEyhfWhVT14WX2/GxgbdwIuxSBcoHlcduca5tKtlKAhJ4kjPPZeQc5A18z48pLRU=";

        String registryURL = "https://registry.hub.docker.com";
        String regUser = "";
        String regPassword = "";
        String repository = "library/alpine";
        String repositoryTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryURL = registryURL;
        String nvRegistryUser = "xxx";
        String nvRegistryPassword = "xxx";
        String mountPath = "/temp";  //mountPath is an optional parameter. It will use "/var/neuvector" by default.
        File file = new File(mountPath + "/scan_result.json");
        Path path = Paths.get(mountPath + "/scan_result.json");
        FileOwnerAttributeView fileOwner = Files.getFileAttributeView(path,
                FileOwnerAttributeView.class);
        UserPrincipal user = fileOwner.getOwner();
        InetAddress addr = InetAddress.getLocalHost();;
        String hostname = addr.getHostName();

        Registry registry = new Registry(registryURL, regUser, regPassword,repository,repositoryTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath);

        ScanRepoReportData scanReportData = Scanner.scanRegistry(registry, scanner, license);

        assertTrue( !hostname.contains("root" ));
        assertTrue( hostname.contains(user.getName()) );
        assertTrue( file.exists() );
        assertTrue( file.canWrite() );
        assertTrue( file.canRead() );
        assertTrue( scanReportData != null );
    }
}
