package com.neuvector.model;

public class ScanModule {
    String name;
    String version;
    String source;
    ModuleCve[] cves;
    String[] cpes;
    
    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /** 
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 
     * @return String
     */
    public String getVersion() {
        return version;
    }
    
    /** 
     * @param version String
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /** 
     * @return String
     */
    public String getSource() {
        return source;
    }
    
    /** 
     * @param source String
     */
    public void setSource(String source) {
        this.source = source;
    }
    
    /** 
     * @return ModuleCve[]
     */
    public ModuleCve[] getCves() {
        return cves;
    }
    
    /** 
     * @param cves ModuleCve[]
     */
    public void setCves(ModuleCve[] cves) {
        this.cves = cves;
    }
    
    /** 
     * @return String[]
     */
    public String[] getCpes() {
        return cpes;
    }
    
    /** 
     * @param cpes String[]
     */
    public void setCpes(String[] cpes) {
        this.cpes = cpes;
    }

    
}
