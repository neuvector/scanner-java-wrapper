## NeuVector Security Scanner ##

You can call NeuVector Scanner APIs to scan the docker registry or local docker image. 

## Prerequisites for the APIs to work ##
1. Docker must be installed on the same machine that the NeuVector Scanner APIs is running because these APIs will run "docker run" on the host machine.

```
docker run --name neuvector.scanner --rm -v "/var/run/docker.sock:/var/run/docker.sock" -v "/var/neuvector:/var/neuvector ... < NeuVector-Scanner-Image >
```

2. Ensure the folder "/var/neuvector" gets created on the machine and make it writable.

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

    //NeuVector license to run the Scanner
    String license = "xxx";  

    // NeuVector Scanner image name. For example, the image name is "neuvector/scanner:latest"
    String nvScannerImage = "neuvector/scanner:latest";

    //The Registry URL from which to pull the NeuVector Scanner image
    //It will NOT do "docker pull" action if nvRegistryUrl, nvRegUser and nvPassword are all empty.
    String nvRegistryUrl = "";

    //Login credentials to the Registry
    String nvRegUser = "";
    String nvPassword = "";

    // The scan result will be returned as a java bean object
    com.neuvector.model.ScanRepoReportData scanReportData = com.neuvector.Scanner.scanLocalImage(imageName, imageTag, license, nvScannerImage, nvRegistryUrl, nvRegUser, nvPassword);
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

    // The scan result will be returned as a java bean object
    com.neuvector.model.ScanRepoReportData scanReportData = com.neuvector.Scanner.scanRegistry(registry, regUser, regPassword, repository, repositoryTag, license, nvScannerImage, nvRegistryUrl, nvRegUser, nvPassword);
```