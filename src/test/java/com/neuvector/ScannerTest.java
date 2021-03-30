package com.neuvector;

import static org.junit.Assert.assertTrue;

import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;

import org.junit.Test;

/**
 * Unit tests
 */
public class ScannerTest 
{
    @Test
    public void scanLocalImageTest()
    {
        String license = "qV9GKO13z+RjlhmAbpoOd1LPzxsi9+4m9iY0HtmB6Wnk0VtQA2bCSQVjm/9uKv2WfbbIYGXg9KQDb8K/Yk71eE+Au6dPdoheipuvv+pgZIxgI/VJKTPqoE/nHyh3ZF2aRct+63TmgNc5GGdSWayzgR69k7Nus1ufQBwxVsmHBJw9LQHyVLzh6pHKRNLeRUy+VzGsO25N6dYSpiz11RiadtQOV8O10a8FXRMFJq5cAy/OPM0xmT1y3KvymITCVFIIac2g/qn9KDOnTHJkOs4yAEVC1NLvdZfJu+gTJ+m5q0TDWvhIE5RwjeeR7pmuUD12+UW4AfCGMUTUpJ49GVssUZBkAG9jMM55/uR3A/YHS3mDiTlRdJhZ+shumT2YOmdt0YuDt+R7NqpH5bukJwGqpgxfo5Atc1eupQFukCfZAcf5MBM8q0wLqDorgDX3AbTizxYDbvccbDcohp2cRCOx+cPOeOqnzpNvp2ZjsDZeDhTqXLNF+Y/dGk3CJ7EiSdX//s3ZXfVthWqlxPYarY3YwKLU/0guWoMT97KTc6+Sp6iZWz2iwLvYG/vo1uJhVtr4eJvVfzGSQVFFJxNgltgAx4CYISPayTZHZxjsJ7PG4qf80L6AL/heNRtmO72hLIB4/AjnkIUB0pifcWfWwG55xP7hmliFKreFKBPi+zW9PNhDFRYdLRxEzXANJLtNbLwZGVlfoykHO0c+0AfYIQWjnsPanpQtT1CLiU0MkYouJIdXzlXxPqucsKzIsgfeFJy3RB+Tr2Y5YA7aLE6wjWqMh2NWnoQSZxbBoVN5kYbhrGqUExq2XUJVYujsP4YSugSLSYk6kkuWDcOaOnCWDxFypAR7aqWnKAnWW6eM6YcLwY9+5Bhbu88i8orW7OZyq9bPpT/NqLt4yruHPiB1PO5g9b4sjlUhzhUrfHl/gL4ySrE1ghVBj6cufJc5DaLaM6yp6sR5Y3ax64O9EtaX57F4KYupLHQ=";

        String imageName = "songlongtj/alpine";
        String imageTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryUser = "xxx";
        String nvRegistryPassword = "xxx";
        String nvRegistryURL = "https://registry.hub.docker.com";

        Image image = new Image(imageName, imageTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword);

        ScanRepoReportData scanReportData = Scanner.scanLocalImage(image,scanner,license);
        assertTrue( scanReportData != null );
    }

    @Test
    public void scanRegistryTest(){
        String license = "qV9GKO13z+RjlhmAbpoOd1LPzxsi9+4m9iY0HtmB6Wnk0VtQA2bCSQVjm/9uKv2WfbbIYGXg9KQDb8K/Yk71eE+Au6dPdoheipuvv+pgZIxgI/VJKTPqoE/nHyh3ZF2aRct+63TmgNc5GGdSWayzgR69k7Nus1ufQBwxVsmHBJw9LQHyVLzh6pHKRNLeRUy+VzGsO25N6dYSpiz11RiadtQOV8O10a8FXRMFJq5cAy/OPM0xmT1y3KvymITCVFIIac2g/qn9KDOnTHJkOs4yAEVC1NLvdZfJu+gTJ+m5q0TDWvhIE5RwjeeR7pmuUD12+UW4AfCGMUTUpJ49GVssUZBkAG9jMM55/uR3A/YHS3mDiTlRdJhZ+shumT2YOmdt0YuDt+R7NqpH5bukJwGqpgxfo5Atc1eupQFukCfZAcf5MBM8q0wLqDorgDX3AbTizxYDbvccbDcohp2cRCOx+cPOeOqnzpNvp2ZjsDZeDhTqXLNF+Y/dGk3CJ7EiSdX//s3ZXfVthWqlxPYarY3YwKLU/0guWoMT97KTc6+Sp6iZWz2iwLvYG/vo1uJhVtr4eJvVfzGSQVFFJxNgltgAx4CYISPayTZHZxjsJ7PG4qf80L6AL/heNRtmO72hLIB4/AjnkIUB0pifcWfWwG55xP7hmliFKreFKBPi+zW9PNhDFRYdLRxEzXANJLtNbLwZGVlfoykHO0c+0AfYIQWjnsPanpQtT1CLiU0MkYouJIdXzlXxPqucsKzIsgfeFJy3RB+Tr2Y5YA7aLE6wjWqMh2NWnoQSZxbBoVN5kYbhrGqUExq2XUJVYujsP4YSugSLSYk6kkuWDcOaOnCWDxFypAR7aqWnKAnWW6eM6YcLwY9+5Bhbu88i8orW7OZyq9bPpT/NqLt4yruHPiB1PO5g9b4sjlUhzhUrfHl/gL4ySrE1ghVBj6cufJc5DaLaM6yp6sR5Y3ax64O9EtaX57F4KYupLHQ=";

        String registryURL = "https://registry.hub.docker.com";
        String regUser = "";
        String regPassword = "";
        String repository = "library/alpine";
        String repositoryTag = "3.6";
        String nvScannerImage = "neuvector/scanner:latest";
        String nvRegistryURL = registryURL;
        String nvRegistryUser = "xxx";
        String nvRegistryPassword = "xxx";

        Registry registry = new Registry(registryURL, regUser, regPassword,repository,repositoryTag);
        NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword);

        ScanRepoReportData scanReportData = Scanner.scanRegistry(registry, scanner, license);
        assertTrue( scanReportData != null );
    }
}
