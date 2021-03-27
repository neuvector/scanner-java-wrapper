package com.neuvector.model;

public class ScanLayer {
    String digest;
    String cmds;
    long  size;
    Vulnerability[] vulnerabilities;

    
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
    public String getCmds() {
        return cmds;
    }
    
    /** 
     * @param cmds
     */
    public void setCmds(String cmds) {
        this.cmds = cmds;
    }
    
    /** 
     * @return long
     */
    public long getSize() {
        return size;
    }
    
    /** 
     * @param size
     */
    public void setSize(long size) {
        this.size = size;
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


    
}
