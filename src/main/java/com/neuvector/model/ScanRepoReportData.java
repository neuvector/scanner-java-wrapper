package com.neuvector.model;

public class ScanRepoReportData {
    String error_message;
    ScanRepoReport report;
    
    
    /** 
     * @return String
     */
    public String getError_message() {
        return error_message;
    }
    
    /** 
     * @param error_message
     */
    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
    
    /** 
     * @return ScanRepoReport
     */
    public ScanRepoReport getReport() {
        return report;
    }
    
    /** 
     * @param report
     */
    public void setReport(ScanRepoReport report) {
        this.report = report;
    }

    
}
