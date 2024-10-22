package com.neuvector.model;

import com.neuvector.Scanner;
import org.slf4j.Logger;

public class NVScanner {
    String nvScannerImage;
    String nvRegistryURL;
    String nvRegistryUser;
    String nvRegistryPassword;
    Logger log;
    String runtime = "docker";
    String socketMapping = Scanner.SOCKET_MAPPING;

    public NVScanner(){}

    /**
     * @param nvScannerImage The name of the NeuVector Scanner image. For example, "neuvector/scanner:latest"
     * @param nvRegistryURL  The registry from which to pull the NeuVector Scanner image. If it is empty, the API will skip the action to pull the NeuVector Scanner image.
     * @param nvRegistryUser  The user name to login the registry. If the user name and the password are empty, the API will skip the docker login action.
     * @param nvRegistryPassword The password to login the registry.
     */
    public NVScanner(String nvScannerImage, String nvRegistryURL, String nvRegistryUser, String nvRegistryPassword, Logger log){
        this.nvRegistryURL = nvRegistryURL;
        this.nvScannerImage = nvScannerImage;
        this.nvRegistryUser = nvRegistryUser;
        this.nvRegistryPassword = nvRegistryPassword;
        this.log = log;
    }

    /**
     *
     * @param nvScannerImage The name of the NeuVector Scanner image. For example, "neuvector/scanner:latest"
     * @param nvRegistryURL  The registry from which to pull the NeuVector Scanner image. If it is empty, the API will skip the action to pull the NeuVector Scanner image.
     * @param nvRegistryUser  The user name to login the registry. If the user name and the password are empty, the API will skip the docker login action.
     * @param nvRegistryPassword The password to login the registry.
     * @param runtime Indicates which runtime platform will be used to invoke the neuvector image. Examples can be docker, podman etc.. If not provided, will default to docker
     * @param socketMapping Socket mapping argument to be used. This is needed for docker in docker environments. Default value is the Scanner.SOCKET_MAPPING constant
     */
    public NVScanner(String nvScannerImage, String nvRegistryURL, String nvRegistryUser, String nvRegistryPassword, Logger log, String runtime, String socketMapping){
        this.nvRegistryURL = nvRegistryURL;
        this.nvScannerImage = nvScannerImage;
        this.nvRegistryUser = nvRegistryUser;
        this.nvRegistryPassword = nvRegistryPassword;
        this.log = log;
        if (runtime != null) {
            this.runtime = runtime;
        }
        if (socketMapping != null) {
            this.socketMapping = socketMapping;
        }
    }

    /**
     * @return String
     */
    public String getNvScannerImage() {
        return nvScannerImage;
    }
    
    /** 
     * @param nvScannerImage The name of the NeuVector Scanner Image
     */
    public void setNvScannerImage(String nvScannerImage) {
        this.nvScannerImage = nvScannerImage;
    }
    
    /** 
     * @return String
     */
    public String getNvRegistryURL() {
        return nvRegistryURL;
    }
    
    /** 
     * @param nvRegistryURL The registry url from which to pull NeuVector Scanner image
     */
    public void setNvRegistryURL(String nvRegistryURL) {
        this.nvRegistryURL = nvRegistryURL;
    }
    
    /** 
     * @return String
     */
    public String getNvRegistryUser() {
        return nvRegistryUser;
    }
    
    /** 
     * @param nvRegistryUser The login user of the registry url
     */
    public void setNvRegistryUser(String nvRegistryUser) {
        this.nvRegistryUser = nvRegistryUser;
    }
    
    /** 
     * @return String
     */
    public String getNvRegistryPassword() {
        return nvRegistryPassword;
    }
    
    /** 
     * @param nvRegistryPassword The login password of the registry url
     */
    public void setNvRegistryPassword(String nvRegistryPassword) {
        this.nvRegistryPassword = nvRegistryPassword;
    }

    public Logger getLog() {
        return log;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getSocketMapping() {
        return socketMapping;
    }
}
