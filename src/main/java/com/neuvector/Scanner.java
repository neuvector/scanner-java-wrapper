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
 * NeuVector Scanner APIs can scan security vulnerabilities of the docker registry or local image. 
 * <p>
 * To scan the docker registry, you can call the API <code> com.neuvector.Scanner.scanRegistry() </code> 
 * <p>
 * To scan the docker local image, you can call the API <code> Scanner.scanLocalImage() </code>
 * <p>
 * It returns a Java bean object <code> com.neuvector.model.ScanRepoReportData </code>
 */
public class Scanner 
{

    private static final String SOCKET_MAPPING = "/var/run/docker.sock:/var/run/docker.sock";
    private static final String PATH_MAPPING = "/var/neuvector:/var/neuvector";
    private static final String SCAN_REPORT= "/var/neuvector/scan_result.json";

    /**
     * To scan a docker registry and return a java bean object of com.neuvector.model.ScanRepoReportData.
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
     * To scan a docker local repository or image and return a java bean object of com.neuvector.model.ScanRepoReportData.
     * 
     * @param imageName
     * @param imageTag
     * @param license
     * @param nvScannerImage
     * @return ScanRepoReportData
     */
    public static ScanRepoReportData scanLocalImage(String imageName, String imageTag, String license, String nvScannerImage) {

        String errorMessage = checkDockerStatus(nvScannerImage);
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + imageName, "-e", "SCANNER_TAG=" + imageTag, "-e", "SCANNER_LICENSE=" + license, nvScannerImage};
            reportData = runScan(cmdArgs);
        }

        return reportData;
    }

        
    /**
     * To check the status of the docker client and the scanner image on the host machine.
     * 
     * @param nvScannerImage
     * @return string. It returns an empty string if the docker status is good. Otherwise, it returns error messages.
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
