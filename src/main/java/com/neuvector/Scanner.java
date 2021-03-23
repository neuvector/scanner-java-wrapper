package com.neuvector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.neuvector.model.ScanRepoReportData;
import net.sf.json.JSONObject;

/**
 * Scanner supports checking security vulnerabilities of the docker registry or repository (image). 
 * <p>
 * It runs <code>docker run --name neuvector.scanner --rm -v "/var/run/docker.sock:/var/run/docker.sock" -v "/var/neuvector:/var/neuvector -e SCANNER_REPOSITORY=REPOSITORY-TO-SCAN -e SCANNER_TAG=REPOSITORY-TO-SCAN-TAG -e SCANNER_LICENSE=NV-LICENSE   -e SCANNER_REGISTRY=REGISTRY-TO-SCAN -e SCANNER_REGISTRY_USERNAME=REGISTRY-LOGIN-USER -e SCANNER_REGISTRY_PASSWORD=REGISTRY-LOGIN-PASSWORD NeuVector-Scanner-Image" </code> on your host machine.
 * Requirements:
 * <ul>
 * <li> <code> docker </code> is runnable and pull the NeuVector Scanner Image to your host machine.
 * <li> You need to create the directory <code> /var/neuvector </code> on your host machine.
 * <li> The application has the permission to create and write file <code> /var/neuvector/scan_result.json </code>.
 * </ul>
 * <p>
 * The method <code> Scanner.scanRegistry(String registryURL, String registryUsername, String registryPassword, String repository, String repositoryTag, String license, String nvScannerImage) </code> check the security vulnerabilities of the docker registry.
 * <p>
 *  * The method <code> Scanner.scanLocalImage(String repository, String repositoryTag, String license, String nvScannerImage) </code> check the security vulnerabilities of the docker local repository or image.
 * 
 * It returns <code> ScanRepoReportData </code> object.
 */
public class Scanner 
{

    private static final String SOCKET_MAPPING = "/var/run/docker.sock:/var/run/docker.sock";
    private static final String PATH_MAPPING = "/var/neuvector:/var/neuvector";
    private static final String SCAN_REPORT= "/var/neuvector/scan_result.json";

    /**
     * To scan a docker registry and return a ScanRepoReportData object.
     * 
     * @param registryURL
     * @param registryUsername
     * @param registryPassword
     * @param repository
     * @param repositoryTag
     * @param license
     * @param nvScannerImage
     * @return ScanRepoReportData
     */
    public static ScanRepoReportData scanRegistry(String registryURL, String registryUsername, String registryPassword, String repository, String repositoryTag, String license, String nvScannerImage) {

        String errorMessage = checkDockerStatus(nvScannerImage);
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + repository, "-e", "SCANNER_TAG=" + repositoryTag, "-e", "SCANNER_LICENSE=" + license, "-e", "SCANNER_REGISTRY=" + registryURL, "-e", "SCANNER_REGISTRY_USERNAME=" + registryUsername, "-e", "SCANNER_REGISTRY_PASSWORD=" + registryPassword, nvScannerImage};
            reportData = runScan(cmdArgs);
        }

        return reportData;
    }

    /**
     * To scan a docker local repository or image and return a ScanRepoReportData object.
     * 
     * @param repository
     * @param repositoryTag
     * @param license
     * @param nvScannerImage
     * @return ScanRepoReportData
     */
    public static ScanRepoReportData scanLocalImage(String repository, String repositoryTag, String license, String nvScannerImage) {

        String errorMessage = checkDockerStatus(nvScannerImage);
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + repository, "-e", "SCANNER_TAG=" + repositoryTag, "-e", "SCANNER_LICENSE=" + license, nvScannerImage};
            reportData = runScan(cmdArgs);
        }

        return reportData;
    }

        
    /**
     * To check the status of the docker client and the scanner image on the host machine.
     * 
     * @param nvScannerImage
     * @return String It is an empty string if the docker is ready to run and the scanner image exists. Otherwise, it returns the error messages.
     */
    private static String checkDockerStatus(String nvScannerImage) {
        String[] cmdArgs = { "docker", "image", "inspect", nvScannerImage };
        return runCMD(cmdArgs);
    }

    private static ScanRepoReportData parseScanReport(){
        ScanRepoReportData scanReportData;
        StringBuilder contentBuilder = new StringBuilder();
        String errorMessage = null;

        try (Stream<String> stream = Files.lines(Paths.get(SCAN_REPORT), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException ex) {
            errorMessage = ex.getMessage();
        }

        if(errorMessage != null){
            scanReportData = new ScanRepoReportData();
            scanReportData.setError_message(errorMessage);
        }else{
            JSONObject jsonObject = JSONObject.fromObject(contentBuilder.toString());
            scanReportData = (ScanRepoReportData) JSONObject.toBean(jsonObject, ScanRepoReportData.class);
        }

        return scanReportData;

    }

    private static String runCMD(String[] cmdArgs){
        Process process;
        String errorMessage = "";
        try {
            process = Runtime.getRuntime().exec(cmdArgs);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // Read the output from the command
            String s = null;
            StringBuilder sb = new StringBuilder(String.join(" ", cmdArgs));
            while ((s = stdInput.readLine()) != null) {
                // sb.append(System.getProperty("line.separator"));
                sb.append(s);
            }

            // Read errors from the command
            while ((s = stdError.readLine()) != null) {
                // sb.append(System.getProperty("line.separator"));
                sb.append(s);
            }

            if (process.waitFor() != 0){
                errorMessage = sb.toString();
            }

        } catch (IOException e) {
            errorMessage = e.getMessage();
        } catch (InterruptedException ex) {
            errorMessage = ex.getMessage();
        }

        return errorMessage;

    }

    private static ScanRepoReportData runScan(String[] cmdArgs){

        String errorMessage = runCMD(cmdArgs);

        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            reportData = parseScanReport();
        }

        return reportData;
    }

}
