package com.neuvector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import net.sf.json.JSONObject;

/**
 * Scanner can check docker registry and repository security vulnerabilities. 
 * <p>
 * It runs <code>docker run --name neuvector.scanner --rm -v "/var/run/docker.sock:/var/run/docker.sock" -v "/var/neuvector:/var/neuvector -e SCANNER_REPOSITORY=REPOSITORY-TO-SCAN -e SCANNER_TAG=REPOSITORY-TO-SCAN-TAG -e SCANNER_LICENSE=NV-LICENSE   -e SCANNER_REGISTRY=REGISTRY-TO-SCAN -e SCANNER_REGISTRY_USERNAME=REGISTRY-LOGIN-USER -e SCANNER_REGISTRY_PASSWORD=REGISTRY-LOGIN-PASSWORD NeuVector-Scanner-Image" </code> on your host machine.
 * Requirements:
 * <ul>
 * <li> <code> docker </code> is runnable and pull the NeuVector Scanner Image to your host machine.
 * <li> You need to create the directory <code> /var/neuvector </code> on your host machine.
 * <li> The application has the permission to create and write file <code> /var/neuvector/scan_result.json </code>.
 * </ul>
 * <p>
 * The method <code> Scanner.scan(ScanConfig config) </code> does the vulnerability scan and returns the scan result as a JSONObject.
 * <p>
 * If the scan fails, the returned JSONObject contains the key <code> error_message </code>
 * 
 * If the scan succeeds, the returned JSONObject contains
 */
public class Scanner 
{

    private static final String SOCKET_MAPPING = "/var/run/docker.sock:/var/run/docker.sock";
    private static final String PATH_MAPPING = "/var/neuvector:/var/neuvector";
    private static final String SCAN_REPORT= "/var/neuvector/scan_result.json";

    /**
     * Check <code> docker </code> client is runnable and nvScannerImage (NeuVector Scanner Image) exists in the docker host.
     * @param nvScannerImage
     * @throws IOException
     * @throws InterruptedException
     */
    public static void checkDockerStatus(String nvScannerImage) throws IOException, InterruptedException{
        String[] cmdArgs = { "docker", "image", "inspect", nvScannerImage };
        Process process = Runtime.getRuntime().exec(cmdArgs);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // Read the output from the command
        String s = null;
        StringBuilder sb = new StringBuilder(String.join(" ", cmdArgs));
        while ((s = stdInput.readLine()) != null) {
            sb.append(s);
        }

        // Read errors from the command
        while ((s = stdError.readLine()) != null) {
            sb.append(s);
        }

        if (process.waitFor() != 0){
            throw new IOException(sb.toString());
        }
    }

    /**
     * Build ScanConfig Object using scan related parameters
     * 
     * @param config
     * @return the scan result as a JSONObject
     */
    public static JSONObject scan(ScanConfig config){

        JSONObject scanResultObject = null;

        try {
            checkDockerStatus(config.getNvScannerImage());
            if(config.getRegistryURL() == null || config.getRegistryURL().isEmpty()){
                scanLocalImage(config.getRepository(), config.getRepositoryTag(), config.getLicense(), config.getNvScannerImage());
            }else{
                scanRegistry(config.getRegistryURL(), config.getRegistryUser(), config.getRegistryPassword(), config.getRepository(), config.getRepositoryTag(), config.getLicense(), config.getNvScannerImage());
            }
        } catch (Exception ex) {
            scanResultObject = new JSONObject().element("error_message", ex.getMessage());
        } finally {
            if (scanResultObject != null){
                return scanResultObject;
            }
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(SCAN_REPORT), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException ex) {
            scanResultObject = new JSONObject().element("error_message", ex.getMessage());
        } finally {
            if (scanResultObject != null){
                return scanResultObject;
            }
        }

        String scanResultJSON = ""; 
        scanResultJSON = contentBuilder.toString();
        scanResultObject = JSONObject.fromObject(scanResultJSON).getJSONObject("report");

        return scanResultObject;

    }

    public static void scanLocalImage(String repository, String repositoryTag, String license, String nvScannerImage) throws IOException, InterruptedException {
        String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + repository, "-e", "SCANNER_TAG=" + repositoryTag, "-e", "SCANNER_LICENSE=" + license, nvScannerImage};

        Process process = Runtime.getRuntime().exec(cmdArgs);

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
            throw new IOException(sb.toString());
        }
    }

    public static void scanRegistry(String registryURL, String registryUsername, String registryPassword, String repository, String repositoryTag, String license, String nvScannerImage) throws IOException, InterruptedException{
        String[] cmdArgs = {"docker", "run", "--name", "neuvector.scanner", "--rm", "-v", Scanner.SOCKET_MAPPING, "-v", Scanner.PATH_MAPPING, "-e", "SCANNER_REPOSITORY=" + repository, "-e", "SCANNER_TAG=" + repositoryTag, "-e", "SCANNER_LICENSE=" + license, "-e", "SCANNER_REGISTRY=" + registryURL, "-e", "SCANNER_REGISTRY_USERNAME=" + registryUsername, "-e", "SCANNER_REGISTRY_PASSWORD=" + registryPassword, nvScannerImage};

        Process process = Runtime.getRuntime().exec(cmdArgs);

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
            throw new IOException(sb.toString());
        }
    }
}
