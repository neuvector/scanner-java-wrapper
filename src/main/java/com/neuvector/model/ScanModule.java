package com.neuvector.model;

public class ScanModule {
    String name;
    String version;
    String source;
    ModuleCve[] cves;
    String[] cpes;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public ModuleCve[] getCves() {
        return cves;
    }
    public void setCves(ModuleCve[] cves) {
        this.cves = cves;
    }
    public String[] getCpes() {
        return cpes;
    }
    public void setCpes(String[] cpes) {
        this.cpes = cpes;
    }

    
}
