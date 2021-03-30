package com.neuvector.model;



public class Registry {
    private String registryURL;
    private String loginUser;
    private String loginPassword;
    private String repository;
    private String repositoryTag;

    public Registry(){}

    public Registry(String registryURL, String loginUser, String loginPassword, String repository, String repositoryTag){
        this.registryURL = registryURL;
        this.loginUser = loginUser;
        this.loginPassword = loginPassword;
        this.repository = repository;
        this.repositoryTag = repositoryTag;
    }
    
    /** 
     * @return String
     */
    public String getRegistryURL() {
        return registryURL;
    }
    
    /** 
     * @param registryURL The registry to be scanned
     */
    public void setRegistryURL(String registryURL) {
        this.registryURL = registryURL;
    }
    
    /** 
     * @return String
     */
    public String getLoginUser() {
        return loginUser;
    }
    
    /** 
     * @param loginUser The login user of the registry to be scanned
     */
    public void setLoginUser(String loginUser) {
        this.loginUser = loginUser;
    }
    
    /** 
     * @return String 
     */
    public String getLoginPassword() {
        return loginPassword;
    }
    
    /** 
     * @param loginPassword The login password of the registry to be scanned
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    
    /** 
     * @return String
     */
    public String getRepository() {
        return repository;
    }
    
    /** 
     * @param repository The repository to be scanned
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }
    
    /** 
     * @return String
     */
    public String getRepositoryTag() {
        return repositoryTag;
    }
    
    /** 
     * @param repositoryTag The tag of the repository to be scanned
     */
    public void setRepositoryTag(String repositoryTag) {
        this.repositoryTag = repositoryTag;
    }


}

