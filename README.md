## NeuVector Security Scanner Integration Module ##

You can call NeuVector Scanner APIs to scan the docker registry or local docker image. 

## Prerequisites for the APIs to work ##
1. Docker must be installed on the same machine that the NeuVector Scanner APIs is running because these APIs will run "docker run" on the host machine.

2. Ensure that you the folder "/var/neuvector" gets created on the machine and make it writable.

3. Have the license to run the NeuVector Scanner

## Usage of NeuVector Scanner APIs ##
add scanner.jar to your classpath.

1. to scan a local image, you can call the API com.neuvector.Scanner.scanLocalImage() 

```
    // the name of the local image to be scanned. 
    // For example, you want to scan a local image "localImage:1.0"
    String imageName = "localImage";

    // the local image tag.  
    String imageTag = "1.0";

    com.neuvector.model.Image image = new Image(imageName, imageTag);

    // NeuVector Scanner image name. For example, the image name is "neuvector/scanner:latest"
    String nvScannerImage = "neuvector/scanner:latest";

    //The Registry URL from which to pull the NeuVector Scanner image
    //It will NOT do "docker pull" action if nvRegistryUrl, nvRegUser and nvPassword are all empty.
    String nvRegistryUrl = "";

    //Login credentials to the Registry
    String nvRegUser = null;
    String nvPassword = null;
    
    //The path to keep the scan report. 
    //When run it in Jenkins plugin, you can use the project's build path as the mountPath
    String mountPath = "/temp"; 
    // If you don't assign a value to it, it will use the default path "/var/neuvector" to save the scan report.
    
    boolean bindMountShared = null|true|false; //value in true, indicates that the bind mount content is shared among multiple containers
    
    // NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, log, bindMountShared)
    com.neuvector.model.NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryURL, nvRegistryUser, nvRegistryPassword, mountPath, bindMountShared);

    //NeuVector license to run the Scanner
    String license = "xxx";  

    // The scan result will be returned as a java bean object
    com.neuvector.model.ScanRepoReportData scanReportData = com.neuvector.Scanner.scanLocalImage(image,scanner,license);
```

2. to scan a registry, you can call the API com.neuvector.Scanner.scanRegistry()

``` 

    // Registry URL to be scanned. For example, the registry url is "https://registry.hub.docker.com" if you want to scan the Docker hub
    String registry_to_scan = "https://registry.hub.docker.com";

    // Login credentials of the registry url. For example, if to scan a public docker repository, the credentials can be empty.
    String regUser = "";
    String regPassword = "";

    // the name of the repository to be scanned. For example, let's scan a public repository "library/alpine:3.6"
    String repository = "library/alpine";

    // the repository tag. 
    String repositoryTag = "3.6";

    com.neuvector.model.Registry registry = new Registry(registry_to_scan, regUser, regPassword,repository,repositoryTag);

    // NeuVector license to run the Scanner
    String license = "xxx"; 

    // NeuVector Scanner image name. For example, the image name is "neuvector/scanner:latest" if you pull it from NeuVector Docker hub.
    String nvScannerImage = "neuvector/scanner:latest";

    //The Registry URL from which to pull the NeuVector Scanner image
    //It will NOT do "docker pull" action if nvRegistryUrl, nvRegUser and nvPassword are all empty.
    String nvRegistryUrl = "";

    //Login credentials to the Registry
    String nvRegUser = "";
    String nvPassword = "";
    String mountPath = "/temp"; //The path to keep the scan report. If you don't assign a value to it, it will use the path "/var/neuvector" by default.
    boolean bindMountShared = null|true|false; //value in true, indicates that the bind mount content is shared among multiple containers
    
    com.neuvector.model.NVScanner scanner = new NVScanner(nvScannerImage, nvRegistryUrl, nvRegUser, nvPassword, mountPath, bindMountShared);

    // The scan result will be returned as a java bean object
    com.neuvector.model.ScanRepoReportData scanReportData = com.neuvector.Scanner.scanRegistry(registry, scanner, license);
```

## License

Copyright © 2016-2022 [NeuVector Inc](https://neuvector.com). All Rights Reserved

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
