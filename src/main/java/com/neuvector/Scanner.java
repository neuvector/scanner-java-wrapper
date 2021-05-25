package com.neuvector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neuvector.model.*;

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
    private static final String CONTAINER_PATH = "/var/neuvector";
    private static final String SCAN_REPORT = "scan_result.json";

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
        if(registry == null || nvScanner == null){
            errorMessage = "The Registry and nvScanner can't be null.";
        }

        
        errorMessage = pullDockerImage(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL(), nvScanner.getNvRegistryUser(), nvScanner.getNvRegistryPassword());
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            if( scanLayers ) {
                String[] cmdArgs = {"docker", "run", "--name", generateScannerName(), "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", getMountPath(nvScanner), "-e", "SCANNER_REPOSITORY=" + registry.getRepository(), "-e", "SCANNER_TAG=" + registry.getRepositoryTag(), "-e", "SCANNER_LICENSE=" + license, "-e", "SCANNER_REGISTRY=" + registry.getRegistryURL(), "-e", "SCANNER_REGISTRY_USERNAME=" + registry.getLoginUser(), "-e", "SCANNER_REGISTRY_PASSWORD=" + registry.getLoginPassword() , "-e", "SCANNER_SCAN_LAYERS=true", getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL())};
                reportData = runScan(cmdArgs, nvScanner);
            }else {
                String[] cmdArgs = {"docker", "run", "--name", generateScannerName(), "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", getMountPath(nvScanner), "-e", "SCANNER_REPOSITORY=" + registry.getRepository(), "-e", "SCANNER_TAG=" + registry.getRepositoryTag(), "-e", "SCANNER_LICENSE=" + license, "-e", "SCANNER_REGISTRY=" + registry.getRegistryURL(), "-e", "SCANNER_REGISTRY_USERNAME=" + registry.getLoginUser(), "-e", "SCANNER_REGISTRY_PASSWORD=" + registry.getLoginPassword() , getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL())};
                reportData = runScan(cmdArgs, nvScanner);
            }

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
        if(image == null || nvScanner == null){
            errorMessage = "The image and nvScanner can't be null.";
        }

        errorMessage = pullDockerImage(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL(), nvScanner.getNvRegistryUser(), nvScanner.getNvRegistryPassword());
        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            if( scanLayers ){
                String[] cmdArgs = {"docker", "run", "--name", generateScannerName(), "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", getMountPath(nvScanner), "-e", "SCANNER_REPOSITORY=" + image.getImageName(), "-e", "SCANNER_TAG=" + image.getImageTag(), "-e", "SCANNER_LICENSE=" + license, "-e", "SCANNER_SCAN_LAYERS=true", getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL())};
                reportData = runScan(cmdArgs, nvScanner);
            }else{
                String[] cmdArgs = {"docker", "run", "--name", generateScannerName(), "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", getMountPath(nvScanner), "-e", "SCANNER_REPOSITORY=" + image.getImageName(), "-e", "SCANNER_TAG=" + image.getImageTag(), "-e", "SCANNER_LICENSE=" + license, getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL())};
                reportData = runScan(cmdArgs, nvScanner);
            }
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

    private static ScanRepoReportData parseScanReport(String scanReportPath){
        ScanRepoReportData scanReportData;
        StringBuilder contentBuilder = new StringBuilder();
        String errorMessage = null;

        try (Stream<String> stream = Files.lines(Paths.get(scanReportPath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException ex) {
            errorMessage = ex.getMessage();
        }

        if(errorMessage != null){
            scanReportData = new ScanRepoReportData();
            scanReportData.setError_message(errorMessage);
        }else{
            scanReportData = new Gson().fromJson(contentBuilder.toString(), ScanRepoReportData.class);
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

    private static ScanRepoReportData runScan(String[] cmdArgs, NVScanner nvScanner){

        String errorMessage = runCMD(cmdArgs);

        ScanRepoReportData reportData = null;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            reportData = parseScanReport(getScanReportPath(nvScanner.getNvMountPath()));
        }

        return reportData;
    }

    private static String getMountPath(NVScanner nvScanner){
        String mountPath = ":" + CONTAINER_PATH;
        String nvPath = nvScanner.getNvMountPath();
        if( nvPath != null && nvPath.length() > 0){
            if( nvPath.charAt(nvPath.length() - 1) == '/' ){
                nvPath = nvPath.substring(0, nvPath.length() - 1);
            }
            mountPath = nvPath + mountPath;
        }else {
            mountPath = CONTAINER_PATH + mountPath;
        }
        return mountPath;
    }

    private static String getScanReportPath(String path){
        String scanReportPath = "";
        if(path == null || path.length() == 0){
            scanReportPath = CONTAINER_PATH + "/" + SCAN_REPORT;
        }else{
            scanReportPath = removeLastSlash(path) + "/" + SCAN_REPORT;
        }

        return scanReportPath;
    }

    private static String removeLastSlash(String str){
        if(str != null && str.length() > 0 && str.charAt(str.length() - 1) == '/'){
            return str.substring(0, str.length() - 1);
        }else{
            return str;
        }
    }

    private static String generateScannerName(){
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 6) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            String saltStr = salt.toString();
            return saltStr;
    }

}
