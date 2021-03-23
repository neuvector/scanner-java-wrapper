### Introduction

Scanner supports checking security vulnerabilities of the docker registry or repository (image). 

It runs <code>docker run --name neuvector.scanner --rm -v "/var/run/docker.sock:/var/run/docker.sock" -v "/var/neuvector:/var/neuvector -e SCANNER_REPOSITORY=REPOSITORY-TO-SCAN -e SCANNER_TAG=REPOSITORY-TO-SCAN-TAG -e SCANNER_LICENSE=NV-LICENSE   -e SCANNER_REGISTRY=REGISTRY-TO-SCAN -e SCANNER_REGISTRY_USERNAME=REGISTRY-LOGIN-USER -e SCANNER_REGISTRY_PASSWORD=REGISTRY-LOGIN-PASSWORD NeuVector-Scanner-Image" </code> on your host machine.

There are two methods can be called. Both of them will return a ScanRepoReportData object.

The first one is to check the security vulnerabilities of the docker registry.

Scanner.scanRegistry(String registryURL, String registryUsername, String registryPassword, String repository, String repositoryTag, String license, String nvScannerImage)

The secode one is to check the security vulnerabilities of the docker local repository or image.

Scanner.scanLocalImage(String repository, String repositoryTag, String license, String nvScannerImage) 

#### Requirements:
1. On your host machine, make sure <code> docker </code> is runnable 
2. Pull the NeuVector Scanner Image to your host machine.
3. Create the directory <code> /var/neuvector </code> on your host machine.
4. Make sure the directory "/var/neuvector" writable to the application, so the scan output file "/var/neuvector/scan_result.json" can be created and read by the application.
