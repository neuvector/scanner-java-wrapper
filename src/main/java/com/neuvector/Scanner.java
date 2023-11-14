package com.neuvector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Random;
import java.util.stream.Stream;

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
        } else {
            errorMessage = pullDockerImage(nvScanner);
        }
        ScanRepoReportData reportData;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            DockerRunCommandBuilder builder = new DockerRunCommandBuilder();
            builder
                .withUserAndGroup(getDockerUserGroupCmdArg(getScanReportPath(nvScanner.getNvMountPath())))
                .withName(generateScannerName())
                .withVolume(Scanner.SOCKET_MAPPING)
                .withVolume(appendSELinuxSuffixIfRequired(nvScanner.isSELinuxSuffixRequired(), getMountPath(nvScanner)))
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
            String[] cmdArgs = builder.buildForImage(getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL()));
            String[] credentials = {registry.getLoginPassword(), license};
            reportData = runScan(cmdArgs, nvScanner, credentials);
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
        } else {
            errorMessage = pullDockerImage(nvScanner);
        }
        ScanRepoReportData reportData;

        if(errorMessage.length() > 0){
            reportData = new ScanRepoReportData();
            reportData.setError_message(errorMessage);
        }else{
            String[] credentials = {license};
            DockerRunCommandBuilder builder = new DockerRunCommandBuilder();
            builder
                .withUserAndGroup(getDockerUserGroupCmdArg(getScanReportPath(nvScanner.getNvMountPath())))
                .withName(generateScannerName())
                .withVolume(Scanner.SOCKET_MAPPING)
                .withVolume(getMountPath(nvScanner))
                .withEnvironment("SCANNER_REPOSITORY=" + image.getImageName())
                .withEnvironment("SCANNER_TAG=" + image.getImageTag())
                .withEnvironment("SCANNER_LICENSE=" + license)
            ;
            if( scanLayers ){
                builder.withEnvironment("SCANNER_SCAN_LAYERS=true");
            }
            String[] cmdArgs = builder.buildForImage(getNVImagePath(nvScanner.getNvScannerImage(), nvScanner.getNvRegistryURL()));
            reportData = runScan(cmdArgs, nvScanner, credentials);
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
            errorMessage = runCMD(cmdArgsDockerLogin, log);
            if(errorMessage.length() == 0){
                String[] cmdArgsDockerPull = {"docker", "pull", getNVImagePath(nvScannerImage, nvRegistryURL)};
                errorMessage = runCMD(cmdArgsDockerPull, log);
                String[] cmdArgsDockerLogout = {"docker", "logout"};
                errorMessage = runCMD(cmdArgsDockerLogout, log);
            }
        } else {
            String[] cmdArgsDockPull = {"docker", "pull", getNVImagePath(nvScannerImage, nvRegistryURL)};
            errorMessage = runCMD(cmdArgsDockPull, log);
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

    private static String runCMD(String[] cmdArgs, Logger log){
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
                if (log != null) {
                    log.info(s);
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

        String errorMessage = runCMD(cmdArgs, nvScanner.getLog());

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

    private static String appendSELinuxSuffixIfRequired(final Boolean isSELinuxSuffixRequired, final String mountPath) {
        if(isSELinuxSuffixRequired != null && isSELinuxSuffixRequired){
            return new StringBuilder(mountPath).append(":z").toString();
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

    private static String maskCredential(String message, String credential){
        return message.replace(credential, "******");
    }

    static String getDockerUserGroupCmdArg(String scanReportPath) {
        // No user arg if file exists, and it is owned by root
        Boolean ownedByRoot = ownedByRoot(scanReportPath);
        if (ownedByRoot != null && ownedByRoot) {
            return null;
        }

        String cmdUserGroupArg = null;
        String UID = executeCommand("id -u");
        if ((UID != null && !UID.isEmpty())) {
            if ("0".equals(UID)) {
                return null; // runs as root
            }
            String GID = executeCommand("stat -c '%g' /var/run/docker.sock");
            if ((GID != null && !GID.isEmpty())) {
                cmdUserGroupArg = UID + ":" + GID.replace("'", "");
            }
        }
        return cmdUserGroupArg;
    }

    private static Boolean ownedByRoot(String scanReportPath)  {
        File scanResultFileJson = new File(scanReportPath);
        try {
            return scanResultsFileExist(scanReportPath) && "root".equals(getUserPrincipal(scanReportPath, scanResultFileJson).getName());
        } catch (IOException e) {
            return null;
        }
    }

    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process proc = Runtime.getRuntime().exec(command);
            getExecValueFromBuffer(output, proc);
        } catch (Exception e) {
            return null;
        }
        return output.toString();
    }

    private static void getExecValueFromBuffer(StringBuilder output, Process proc) throws IOException, InterruptedException {
        String line;
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            proc.waitFor();
        }
    }

    private static boolean scanResultsFileExist(String scanResultFilePath) {
        File scanResultFile = new File(scanResultFilePath);
        return scanResultFile.exists();
    }

    private static UserPrincipal getUserPrincipal(String mountPath, File file) throws IOException {
        UserPrincipal user = null;
        if (file.exists()) {
            Path path = Paths.get(mountPath);
            FileOwnerAttributeView fileOwner = Files.getFileAttributeView(path,
                    FileOwnerAttributeView.class);
            user = fileOwner.getOwner();
        }
        return user;
    }

    public static String deleteDockerImagesByLabelKey(String label) {
        String errorMessage = "";
        String[] cmdArgsDockerDelete = {"docker", "image", "prune", "--force", "--filter=label=".concat(label)};
        errorMessage = runCMD(cmdArgsDockerDelete, null);
        return errorMessage;
    }

}
