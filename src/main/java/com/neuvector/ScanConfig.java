package com.neuvector;

/**
 * ScanConnfig is an object to store all scan related parameters 
 */

public class ScanConfig {
    private String registryURL;
    private String registryUser;
    private String registryPassword;
    private String license;
    private String repository;
    private String repositoryTag;
    private String nvScannerImage;

    /**
     * Creates a ScanConfig from a ConfigBuilder
     * @param builder
     */
    private ScanConfig(ConfigBuilder builder){
        this.registryURL = builder.registryURL;
        this.registryUser = builder.registryUser;
        this.registryPassword = builder.registryPassword;
        this.license = builder.license;
        this.repository = builder.repository;
        this.repositoryTag = builder.repositoryTag;
        this.nvScannerImage = builder.nvScannerImage;
    }

    public String getRegistryURL(){
        return this.registryURL;
    }

    public String getRegistryUser(){
        return this.registryUser;
    }

    public String getRegistryPassword(){
        return this.registryPassword;
    }

    public String getLicense(){
        return this.license;
    }

    public String getRepository(){
        return this.repository;
    }

    public String getRepositoryTag(){
        return this.repositoryTag;
    }

    public String getNvScannerImage(){
        return this.nvScannerImage;
    }
    

    public static class ConfigBuilder {
        private final String repository;
        private final String repositoryTag;
        private final String nvScannerImage;
        private final String license;
        private String registryURL;
        private String registryUser;
        private String registryPassword;

        public ConfigBuilder(String repository, String repositoryTag, String license, String nvScannerImage){
            this.repository = repository;
            this.repositoryTag = repositoryTag;
            this.license = license;
            this.nvScannerImage = nvScannerImage;
        }
        
        public ScanConfig build() {
            ScanConfig config = new ScanConfig(this);
            return config;
        }

        public ConfigBuilder registryURL(String registryURL) {
            this.registryURL = registryURL;
            return this;
        }

        public ConfigBuilder registryUser(String registryUser) {
            this.registryUser = registryUser;
            return this;
        }

        public ConfigBuilder registryPassword(String registryPassword) {
            this.registryPassword = registryPassword;
            return this;
        }
    }
}
