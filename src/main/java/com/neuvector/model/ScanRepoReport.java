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

    public String getVerdict() {
        return verdict;
    }
    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }
    public String getImage_id() {
        return image_id;
    }
    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
    public String getRegistry() {
        return registry;
    }
    public void setRegistry(String registry) {
        this.registry = registry;
    }
    public String getRepository() {
        return repository;
    }
    public void setRepository(String repository) {
        this.repository = repository;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getDigest() {
        return digest;
    }
    public void setDigest(String digest) {
        this.digest = digest;
    }
    public String getBase_os() {
        return base_os;
    }
    public void setBase_os(String base_os) {
        this.base_os = base_os;
    }
    public String getCvedb_version() {
        return cvedb_version;
    }
    public void setCvedb_version(String cvedb_version) {
        this.cvedb_version = cvedb_version;
    }
    public String getCvedb_create_time() {
        return cvedb_create_time;
    }
    public void setCvedb_create_time(String cvedb_create_time) {
        this.cvedb_create_time = cvedb_create_time;
    }
    public ScanLayer[] getLayers() {
        return layers;
    }
    public void setLayers(ScanLayer[] layers) {
        this.layers = layers;
    }
    public Vulnerability[] getVulnerabilities() {
        return vulnerabilities;
    }
    public void setVulnerabilities(Vulnerability[] vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }
    public ScanModule[] getModules() {
        return modules;
    }
    public void setModules(ScanModule[] modules) {
        this.modules = modules;
    }
    public BenchItem[] getChecks() {
        return checks;
    }
    public void setChecks(BenchItem[] checks) {
        this.checks = checks;
    }
    public ScanSecret[] getSecrets() {
        return secrets;
    }
    public void setSecrets(ScanSecret[] secrets) {
        this.secrets = secrets;
    }
    public ScanSetIdPerm[] getSetid_perms() {
        return setid_perms;
    }
    public void setSetid_perms(ScanSetIdPerm[] setid_perms) {
        this.setid_perms = setid_perms;
    }
    public String[] getEnvs() {
        return envs;
    }
    public void setEnvs(String[] envs) {
        this.envs = envs;
    }
    public Map<String, String> getLabels() {
        return labels;
    }
    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }
    public String[] getCmds() {
        return cmds;
    }
    public void setCmds(String[] cmds) {
        this.cmds = cmds;
    }

    
}
