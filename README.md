## NeuVector Security Scanner ##

You can call NeuVector Scanner APIs to scan the docker registry or local docker image. 

## Prerequisites for the APIs to work ##
1. Docker must be installed on the same machine that the NeuVector Scanner APIs is going to run because these APIs will run "docker run" on the host machine.

```
docker run --name neuvector.scanner --rm -v "/var/run/docker.sock:/var/run/docker.sock" -v "/var/neuvector:/var/neuvector ... < NeuVector-Scanner-Image >
```

2. Ensure NeuVector Scanner image exists on the machine, you will need permission to pull the image either from NeuVector docker hub or from your private repository.

```
sudo docker pull < NeuVector's Scanner Image >
```

3. Ensure the folder "/var/neuvector" gets created on the machine and make it writable for the NeuVector Scanner Container because the Scanner will generate an outfile "/var/neuvector/scan_result.json"

4. Have the license to run the NeuVector Scanner

## Usage of NeuVector Scanner APIs ##
add scanner.jar to your classpath.

1. to scan a local image, you can call the API com.neuvector.Scanner.scanLocalImage() 
```
    //NeuVector license to run the Scanner
    String license = "xxx";  

    // the name of the local image to be scanned. For example, you want to scan a local image "alpine:3.6"
    String imageName = "alpine";

    // the local image tag.  
    String imageTag = "3.6";

    // NeuVector Scanner image name. For example, the image name is "neuvector/scanner:latest" if you pull it from NeuVector Docker hub.
    String nvScannerImage = "neuvector/scanner:latest";

    // Scan result gets returned as a java bean object
    com.neuvector.model.ScanRepoReportData scanReportData = com.neuvector.Scanner.scanLocalImage(imageName, imageTag, license, nvScannerImage);
```

2. to scan a registry, you can call the API com.neuvector.Scanner.scanRegistry()

```
    // NeuVector license to run the Scanner
    String license = "xxx";  

    // Registry URL to be scanned. For example, the registry url is "https://registry.hub.docker.com" if you want to scan the Docker hub
    String registry = "https://registry.hub.docker.com";

    // Login User of the registry url. For example, the login user and password are empty if you want to scan a public repository on the Docker hub
    String regUser = "";
    String regPassword = "";

    // the name of the repository to be scanned. For example, you want to scan a public repository "library/alpine:3.6"
    String repository = "library/alpine";

    // the repository tag. 
    String repositoryTag = "3.6";

    // NeuVector Scanner image name. For example, the image name is "neuvector/scanner:latest" if you pull it from NeuVector Docker hub.
    String nvScannerImage = "neuvector/scanner:latest";

    // Scan result gets returned as a java bean object
    com.neuvector.model.ScanRepoReportData scanReportData = com.neuvector.Scanner.scanRegistry(registry, regUser, regPassword, repository, repositoryTag, license, nvScannerImage);
```