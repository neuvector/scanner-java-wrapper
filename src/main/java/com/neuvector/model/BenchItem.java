package com.neuvector.model;

public class BenchItem{
    String test_number;
    String catalog;
    String type;
    String profile;
    boolean scored;
    String description;
    String remediation;
    String[] tags;
    String level;
    String evidence;
    String location;
    String[] message;
    String group;

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getEvidence() {
        return evidence;
    }
    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String[] getMessage() {
        return message;
    }
    public void setMessage(String[] message) {
        this.message = message;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getTest_number() {
        return test_number;
    }
    public void setTest_number(String test_number) {
        this.test_number = test_number;
    }
    public String getCatalog() {
        return catalog;
    }
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
    public boolean isScored() {
        return scored;
    }
    public void setScored(boolean scored) {
        this.scored = scored;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRemediation() {
        return remediation;
    }
    public void setRemediation(String remediation) {
        this.remediation = remediation;
    }
    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    
}
