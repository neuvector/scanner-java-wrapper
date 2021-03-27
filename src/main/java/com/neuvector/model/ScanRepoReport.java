package com.neuvector.model;

import java.util.Map;

public class ScanRepoReport {
    String  verdict;
    String  image_id;
    String  registry;
    String  repository;
    String  tag;
    String  digest;
    String  base_os;
    String  cvedb_version;
    String  cvedb_create_time;
    ScanLayer[] layers;
    Vulnerability[] vulnerabilities;
    ScanModule[] modules;
    BenchItem[] checks;
    ScanSecret[] secrets;
    ScanSetIdPerm[] setid_perms;
    String[] envs;
    Map<String, String> labels;
    String[] cmds;

    
    /** 
     * @return String
     */
    public String getVerdict() {
        return verdict;
    }
    
    /** 
     * @param verdict
     */
    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }
    
    /** 
     * @return String
     */
    public String getImage_id() {
        return image_id;
    }
    
    /** 
     * @param image_id
     */
    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
    
    /** 
     * @return String
     */
    public String getRegistry() {
        return registry;
    }
    
    /** 
     * @param registry
     */
    public void setRegistry(String registry) {
        this.registry = registry;
    }
    
    /** 
     * @return String
     */
    public String getRepository() {
        return repository;
    }
    
    /** 
     * @param repository
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }
    
    /** 
     * @return String
     */
    public String getTag() {
        return tag;
    }
    
    /** 
     * @param tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    /** 
     * @return String
     */
    public String getDigest() {
        return digest;
    }
    
    /** 
     * @param digest
     */
    public void setDigest(String digest) {
        this.digest = digest;
    }
    
    /** 
     * @return String
     */
    public String getBase_os() {
        return base_os;
    }
    
    /** 
     * @param base_os
     */
    public void setBase_os(String base_os) {
        this.base_os = base_os;
    }
    
    /** 
     * @return String
     */
    public String getCvedb_version() {
        return cvedb_version;
    }
    
    /** 
     * @param cvedb_version
     */
    public void setCvedb_version(String cvedb_version) {
        this.cvedb_version = cvedb_version;
    }
    
    /** 
     * @return String
     */
    public String getCvedb_create_time() {
        return cvedb_create_time;
    }
    
    /** 
     * @param cvedb_create_time
     */
    public void setCvedb_create_time(String cvedb_create_time) {
        this.cvedb_create_time = cvedb_create_time;
    }
    
    /** 
     * @return ScanLayer[]
     */
    public ScanLayer[] getLayers() {
        return layers;
    }
    
    /** 
     * @param layers
     */
    public void setLayers(ScanLayer[] layers) {
        this.layers = layers;
    }
    
    /** 
     * @return Vulnerability[]
     */
    public Vulnerability[] getVulnerabilities() {
        return vulnerabilities;
    }
    
    /** 
     * @param vulnerabilities
     */
    public void setVulnerabilities(Vulnerability[] vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }
    
    /** 
     * @return ScanModule[]
     */
    public ScanModule[] getModules() {
        return modules;
    }
    
    /** 
     * @param modules
     */
    public void setModules(ScanModule[] modules) {
        this.modules = modules;
    }
    
    /** 
     * @return BenchItem[]
     */
    public BenchItem[] getChecks() {
        return checks;
    }
    
    /** 
     * @param checks
     */
    public void setChecks(BenchItem[] checks) {
        this.checks = checks;
    }
    
    /** 
     * @return ScanSecret[]
     */
    public ScanSecret[] getSecrets() {
        return secrets;
    }
    
    /** 
     * @param secrets
     */
    public void setSecrets(ScanSecret[] secrets) {
        this.secrets = secrets;
    }
    
    /** 
     * @return ScanSetIdPerm[]
     */
    public ScanSetIdPerm[] getSetid_perms() {
        return setid_perms;
    }
    
    /** 
     * @param setid_perms
     */
    public void setSetid_perms(ScanSetIdPerm[] setid_perms) {
        this.setid_perms = setid_perms;
    }
    
    /** 
     * @return String[]
     */
    public String[] getEnvs() {
        return envs;
    }
    
    /** 
     * @param envs
     */
    public void setEnvs(String[] envs) {
        this.envs = envs;
    }
    
    /** 
     * @return Map<String, String>
     */
    public Map<String, String> getLabels() {
        return labels;
    }
    
    /** 
     * @param labels
     */
    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }
    
    /** 
     * @return String[]
     */
    public String[] getCmds() {
        return cmds;
    }
    
    /** 
     * @param cmds
     */
    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }

    
}
