package com.neuvector.model;

public class ScanLayer {
    String digest;
    String cmds;
    long  size;
    Vulnerability[] vulnerabilities;

    public String getDigest() {
        return digest;
    }
    public void setDigest(String digest) {
        this.digest = digest;
    }
    public String getCmds() {
        return cmds;
    }
    public void setCmds(String cmds) {
        this.cmds = cmds;
    }
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public Vulnerability[] getVulnerabilities() {
        return vulnerabilities;
    }
    public void setVulnerabilities(Vulnerability[] vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }


    
}
