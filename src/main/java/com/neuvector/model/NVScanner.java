package com.neuvector.model;

public class NVScanner {
    String nvScannerImage;
    String nvRegistryURL;
    String nvRegistryUser;
    String nvRegistryPassword;
    
    public NVScanner(){}

    public NVScanner(String nvScannerImage, String nvRegistryURL, String nvRegistryUser, String nvRegistryPassword){
        this.nvRegistryURL = nvRegistryURL;
        this.nvScannerImage = nvScannerImage;
        this.nvRegistryUser = nvRegistryUser;
        this.nvRegistryPassword = nvRegistryPassword;
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

    
    
}
