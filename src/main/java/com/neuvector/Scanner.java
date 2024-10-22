package com.neuvector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;

import org.slf4j.Logger;

/**
 * NeuVector Scanner APIs can scan security vulnerabilities of the local docker image or the docker registry. 
 * <p>
 * To scan the docker local image, you can call the API <code> Scanner.scanLocalImage() </code>
 * <p>
 * To scan the docker registry, you can call the API <code> com.neuvector.Scanner.scanRegistry() </code> 
 * <p>
 * It returns a Java bean object <code> com.neuvector.model.ScanRepoReportData </code>
 */
public class Scanner 
{

    public static final String SOCKET_MAPPING = "/var/run/docker.sock:/var/run/docker.sock";

     /**
      * To scan a docker registry and return a java bean object of com.neuvector.model.ScanRepoReportData.
      * 
      * @param registry The registry object to be scanned
      * @param nvScanner The NeuVector Scanner object
      * @param license The String typed license to run the NeuVector Scanner
      * @param scanLayers Scan image layers 
      * @return ScanRepoReportData
      */
    public static ScanRepoReportData scanRegistry(Registry registry, NVScanner nvScanner, String license, Boolean scanLayers) {

        String errorMessage = "";
        Logger log = nvScanner.getLog();
        if(registry == null || nvScanner == null){
            errorMessage = "The Registry and nvScanner can't be null.";
        } else {
            errorMessage = pullDockerImage(nvScanner);
        }
        ScanRepoReportData reportData;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            DockerRunCommandBuilder builder = new DockerRunCommandBuilder(nvScanner.getRuntime());
            builder
                .withEntrypoint("/usr/bin/bash")
                .withVolume(nvScanner.getSocketMapping())
                .withEnvironment("SCANNER_REPOSITORY=" + registry.getRepository())
                .withEnvironment("SCANNER_TAG=" + registry.getRepositoryTag())
                .withEnvironment("SCANNER_LICENSE=" + license)
                .withEnvironment("SCANNER_REGISTRY=" + registry.getRegistryURL())
            ;
            if( scanLayers ) {
                builder.withEnvironment("SCANNER_SCAN_LAYERS=true");
            }
            if (registry.getLoginUser() != null || registry.getLoginPassword() != null) {
                builder
                    .withEnvironment("SCANNER_REGISTRY_USERNAME=" + registry.getLoginUser())
                    .withEnvironment("SCANNER_REGISTRY_PASSWORD=" + registry.getLoginPassword());
            }
            builder.withImage(getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL()));
            builder.withCommand("/usr/local/bin/monitor && echo 'scan_result.json' && cat /var/neuvector/scan_result.json");
            String[] credentials = {registry.getLoginPassword(), license};
            String[] runCmd = builder.getCmdList().toArray(new String[0]);
            log.info("Running container scan via: {}", String.join(" ", masked(runCmd)));
            reportData = runScan(runCmd, nvScanner, credentials);
        }

        return reportData;
    }

    /**
      * To scan a docker registry and return a java bean object of com.neuvector.model.ScanRepoReportData.
      * 
      * @param registry The registry object to be scanned
      * @param nvScanner The NeuVector Scanner object
      * @param license The String typed license to run the NeuVector Scanner
      * @return ScanRepoReportData
      */
      public static ScanRepoReportData scanRegistry(Registry registry, NVScanner nvScanner, String license) {
          return scanRegistry(registry, nvScanner, license, false);
      }

     /**
      * To scan a docker local image and return a java bean object of com.neuvector.model.ScanRepoReportData.
      * 
      * @param image The image object to be scanned
      * @param nvScanner The NeuVector Scanner object
      * @param license The String typed license to run the NeuVector Scanner
      * @param scanLayers Scan image layers 
      * @return ScanRepoReportData
      */
    public static ScanRepoReportData scanLocalImage(Image image, NVScanner nvScanner, String license, Boolean scanLayers) {

        String errorMessage = "";
        Logger log = nvScanner.getLog();
        if(image == null || nvScanner == null){
            errorMessage = "The image and nvScanner can't be null.";
        } else {
            errorMessage = pullDockerImage(nvScanner);
        }
        ScanRepoReportData reportData;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] credentials = {license};
            DockerRunCommandBuilder builder = new DockerRunCommandBuilder(nvScanner.getRuntime());
            builder
                .withEntrypoint("/usr/bin/bash")
                .withVolume(nvScanner.getSocketMapping())
                .withEnvironment("SCANNER_REPOSITORY=" + image.getImageName())
                .withEnvironment("SCANNER_TAG=" + image.getImageTag())
                .withEnvironment("SCANNER_LICENSE=" + license)
            ;
            if( scanLayers ){
                builder.withEnvironment("SCANNER_SCAN_LAYERS=true");
            }
            builder.withImage(getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL()));
            builder.withCommand("/usr/local/bin/monitor && echo 'scan_result.json' && cat /var/neuvector/scan_result.json");
            String[] runCmd = builder.getCmdList().toArray(new String[0]);
            log.info("Running container scan via: {}", String.join(" ", masked(runCmd)));
            reportData = runScan(runCmd, nvScanner, credentials);
        }

        return reportData;
    }

     /**
      * To scan a docker local image and return a java bean object of com.neuvector.model.ScanRepoReportData.
      * 
      * @param image The image object to be scanned
      * @param nvScanner The NeuVector Scanner object
      * @param license The String typed license to run the NeuVector Scanner
      * @return ScanRepoReportData
      */
      public static ScanRepoReportData scanLocalImage(Image image, NVScanner nvScanner, String license) {
          return scanLocalImage(image, nvScanner, license, false);
      }    

    private static String pullDockerImage(NVScanner nvScanner) {
        String nvRegistryURL = nvScanner.getNvRegistryURL();
        String nvRegistryUser = nvScanner.getNvRegistryUser();
        String nvRegistryPassword = nvScanner.getNvRegistryPassword();
        String nvScannerImage = nvScanner.getNvScannerImage();
        Logger log = nvScanner.getLog();

        if(nvRegistryURL == null){
            nvRegistryURL = "";
        }

        if(nvRegistryUser == null){
            nvRegistryUser = "";
        }

        if(nvRegistryPassword == null){
            nvRegistryPassword = "";
        }

        String errorMessage = "";

        if(nvRegistryURL.isEmpty() && nvRegistryUser.isEmpty() && nvRegistryPassword.isEmpty()){
            return errorMessage;
        }

        if(!nvRegistryUser.equals("") && !nvRegistryPassword.equals("")){
            String[] cmdArgsDockerLogin = {"docker", "login", "-u", nvRegistryUser, "-p", nvRegistryPassword, nvRegistryURL};
            errorMessage = runCMD(cmdArgsDockerLogin, log, null);
            if(errorMessage.length() == 0){
                String[] cmdArgsDockerPull = {"docker", "pull", getNVImagePath(nvScannerImage, nvRegistryURL)};
                errorMessage = runCMD(cmdArgsDockerPull, log, null);
                String[] cmdArgsDockerLogout = {"docker", "logout"};
                errorMessage = errorMessage + "" + runCMD(cmdArgsDockerLogout, log, null);
            }
        } else {
            String[] cmdArgsDockPull = {"docker", "pull", getNVImagePath(nvScannerImage, nvRegistryURL)};
            errorMessage = runCMD(cmdArgsDockPull, log, null);
        }

        // mask the password in the error message
        if(! (errorMessage.isEmpty() || nvRegistryPassword.isEmpty()) ){
            errorMessage = maskCredential(errorMessage, nvRegistryPassword);
        }

        return errorMessage;
    }

    private static String getNVImagePath(String nvScannerImage, String nvRegistryURL){

        String nvImagePath = "";

        if(nvRegistryURL.isEmpty()){
            nvImagePath = nvScannerImage;
        }else{
            if(nvRegistryURL.contains("//")){
                nvImagePath = nvRegistryURL.split("//")[1];
            }else{
                nvImagePath = nvRegistryURL;
            }
            if(nvRegistryURL.endsWith("/")){
                nvImagePath = nvImagePath + nvScannerImage;
            }else {
                nvImagePath = nvImagePath + "/" + nvScannerImage;
            }
        }
        return nvImagePath;
    }

    private static ScanRepoReportData parseScanReport(String reportJson){
        ScanRepoReportData scanReportData = new Gson().fromJson(reportJson, ScanRepoReportData.class);
        return scanReportData;
    }

    private static String runCMD(String[] cmdArgs, Logger log, StringBuilder outputCollector){
        Process process;
        String errorMessage = "";
        try {
            process = Runtime.getRuntime().exec(cmdArgs);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // Read the output from the command
            String s = null;
            StringBuilder sb = new StringBuilder(String.join(" ", cmdArgs));
            boolean catScanResultJsonOutput = false;
            while ((s = stdInput.readLine()) != null) {
                if (outputCollector != null) {
                    outputCollector.append(s);
                }
                // sb.append(System.getProperty("line.separator"));
                if (log != null) {
                    if ("scan_result.json".equals(s)) {
                        // Do not print out scan_result.json in logs as it might get quite large (thousands of lines)
                        // Stop logging after we encounter scan_result.json
                        catScanResultJsonOutput = true;
                        if (!log.isDebugEnabled()) {
                            log.info("If debug mode is enabled, the contents of scan_result.json can be observed in the logs.");
                            log.info("Contents of scan_result.json are not included at info level.");
                        }
                    }
                    if (catScanResultJsonOutput) {
                        log.debug(s);
                    } else {
                        log.info(s);
                    }
                }
                sb.append(s);
            }

            // Read errors from the command
            while ((s = stdError.readLine()) != null) {
                // sb.append(System.getProperty("line.separator"));
                if (log != null) {
                    log.error(s);
                }
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

    private static ScanRepoReportData runScan(String[] cmdArgs, NVScanner nvScanner, String[] credentials) {
        StringBuilder outputCollector = new StringBuilder();
        String errorMessage = runCMD(cmdArgs, nvScanner.getLog(), outputCollector);

        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            for (String credential : credentials) {
                if(!credential.isEmpty()){
                    errorMessage = maskCredential(errorMessage, credential);
                }
                
            }
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String scanResultJson = "scan_result.json";
            String reportJsonString = outputCollector.substring(outputCollector.toString().indexOf(scanResultJson) + scanResultJson.length());
            reportData = parseScanReport(reportJsonString);
        }

        return reportData;
    }

    private static String maskCredential(String message, String credential){
        return message.replace(credential, "******");
    }

    public static String[] masked(String[] runCmd) {
        String[] masked = new String[runCmd.length];

        for (int i = 0; i < runCmd.length; i++) {
            masked[i] = runCmd[i];
            if (masked[i] != null && masked[i].startsWith("SCANNER_REGISTRY_PASSWORD=")) {
                masked[i] = "SCANNER_REGISTRY_PASSWORD=****";
            }
        }

        return masked;
    }
}
