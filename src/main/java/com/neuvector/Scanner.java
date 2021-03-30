package com.neuvector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.neuvector.model.Image;
import com.neuvector.model.NVScanner;
import com.neuvector.model.Registry;
import com.neuvector.model.ScanRepoReportData;
import net.sf.json.JSONObject;

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

    private static final String SOCKET_MAPPING = "/var/run/docker.sock:/var/run/docker.sock";
    private static final String PATH_MAPPING = "/var/neuvector:/var/neuvector";
    private static final String SCAN_REPORT = "/var/neuvector/scan_result.json";

     /**
      * To scan a docker registry and return a java bean object of com.neuvector.model.ScanRepoReportData.
      * 
      * @param registry The registry object to be scanned
      * @param nvScanner The NeuVector Scanner object
      * @param license The String typed license to run the NeuVector Scanner
      * @return ScanRepoReportData
      */
    public static ScanRepoReportData scanRegistry(Registry registry, NVScanner nvScanner, String license) {

        String errorMessage = "";
        if(registry == null || nvScanner == null){
            errorMessage = "To call scanRegistry() API, the Registry and nvScanner can't be null";
        }

        
        errorMessage = pullDockerImage(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL(), nvScanner.getNvRegistryUser(), nvScanner.getNvRegistryPassword());
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + registry.getRepository(), "-e", "SCANNER_TAG=" + registry.getRepositoryTag(), "-e", "SCANNER_LICENSE=" + license, "-e", "SCANNER_REGISTRY=" + registry.getRegistryURL(), "-e", "SCANNER_REGISTRY_USERNAME=" + registry.getLoginUser(), "-e", "SCANNER_REGISTRY_PASSWORD=" + registry.getLoginPassword() , getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL())};
            reportData = runScan(cmdArgs);
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

        String errorMessage = "";
        if(image == null || nvScanner == null){
            errorMessage = "To call scanLocalImage() API, the image and nvScanner object can't be null.";
        }

        errorMessage = pullDockerImage(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL(), nvScanner.getNvRegistryUser(), nvScanner.getNvRegistryPassword());
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + image.getImageName(), "-e", "SCANNER_TAG=" + image.getImageTag(), "-e", "SCANNER_LICENSE=" + license, getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL())};
            reportData = runScan(cmdArgs);
        }

        return reportData;
    }

    private static String pullDockerImage(String nvScannerImage, String nvRegistryURL, String nvRegistryUser, String nvRegistryPassword) {

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

        if(nvRegistryUser != "" && nvRegistryPassword != ""){
            String[] cmdArgsDockerLogin = {"docker", "login", "-u", nvRegistryUser, "-p", nvRegistryPassword, nvRegistryURL};
            errorMessage = runCMD(cmdArgsDockerLogin);
            if(errorMessage.length() == 0){
                String[] cmdArgsDockerPull = {"docker", "pull", getNVImagePath(nvScannerImage, nvRegistryURL)};
                errorMessage = runCMD(cmdArgsDockerPull);
                String[] cmdArgsDockerLogout = {"docker", "logout"};
                errorMessage = runCMD(cmdArgsDockerLogout);
            }
        }else{
            String[] cmdArgsDockPull = {"docker", "pull", getNVImagePath(nvScannerImage, nvRegistryURL)};
            errorMessage = runCMD(cmdArgsDockPull);
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
            }
            if(nvRegistryURL.endsWith("/")){
                nvImagePath = nvImagePath + nvScannerImage;
            }else {
                nvImagePath = nvImagePath + "/" + nvScannerImage;
            }
        }
        return nvImagePath;
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
