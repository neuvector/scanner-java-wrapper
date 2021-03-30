package com.neuvector.model;

public class Image {
    String imageName;
    String imageTag;

    public Image(){}

    public Image(String imageName, String imageTag){
        this.imageName = imageName;
        this.imageTag = imageTag;
    }

    
    /** 
     * @return String
     */
    public String getImageName() {
        return imageName;
    }
    
    /** 
     * @param imageName The name of the local image which is going to be scanned
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    
    /** 
     * @return String
     */
    public String getImageTag() {
        return imageTag;
    }
    
    /** 
     * @param imageTag The local image tag
     */
    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    
    
}
