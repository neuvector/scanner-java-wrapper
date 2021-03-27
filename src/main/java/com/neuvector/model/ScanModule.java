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
     * @param name
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
     * @param version
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
     * @param source
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
     * @param cves
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
     * @param cpes
     */
    public void setCpes(String[] cpes) {
        this.cpes = cpes;
    }

    
}
