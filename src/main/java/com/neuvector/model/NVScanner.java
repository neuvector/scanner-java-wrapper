package com.neuvector.model;

public class NVScanner {
    String nvScannerImage;
    String nvRegistryURL;
    String nvRegistryUser;
    String nvRegistryPassword;
    String nvMountPath;
    
    public NVScanner(){}

    /**
     * @param nvScannerImage The name of the NeuVector Scanner image. For example, "neuvector/scanner:latest"
     * @param nvRegistryURL  The registry from which to pull the NeuVector Scanner image. If it is empty, the API will skip the action to pull the NeuVector Scanner image.
     * @param nvRegistryUser  The user name to login the registry. If the user name and the password are empty, the API will skip the docker login action.
     * @param nvRegistryPassword The password to login the registry.
     */
    public NVScanner(String nvScannerImage, String nvRegistryURL, String nvRegistryUser, String nvRegistryPassword){
        this.nvRegistryURL = nvRegistryURL;
        this.nvScannerImage = nvScannerImage;
        this.nvRegistryUser = nvRegistryUser;
        this.nvRegistryPassword = nvRegistryPassword;
    }

    /**
     *
     * @param nvScannerImage The name of the NeuVector Scanner image. For example, "neuvector/scanner:latest"
     * @param nvRegistryURL  The registry from which to pull the NeuVector Scanner image. If it is empty, the API will skip the action to pull the NeuVector Scanner image.
     * @param nvRegistryUser  The user name to login the registry. If the user name and the password are empty, the API will skip the docker login action.
     * @param nvRegistryPassword The password to login the registry.
     * @param nvMountPath  The mount path mapping to the path inside the NeuVector Scanner container. It is the path to store the scan result. It is optional. If you don't pass in <code>nvMountPath</code>, it will use the default path "/var/neuvector"
     */
    public NVScanner(String nvScannerImage, String nvRegistryURL, String nvRegistryUser, String nvRegistryPassword, String nvMountPath){
        this.nvRegistryURL = nvRegistryURL;
        this.nvScannerImage = nvScannerImage;
        this.nvRegistryUser = nvRegistryUser;
        this.nvRegistryPassword = nvRegistryPassword;
        this.nvMountPath = nvMountPath;
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

    /**
     * @return String
     */
    public String getNvMountPath() {
        return nvMountPath;
    }

    /**
     * @param nvMountPath The file path to save the scan report.
     */
    public void setNvMountPath(String nvMountPath) {
        this.nvMountPath = nvMountPath;
    }
    
    
}
