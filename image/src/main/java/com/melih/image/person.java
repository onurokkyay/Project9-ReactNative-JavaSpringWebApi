package com.melih.image;

import java.util.ArrayList;

public class person {

	
	private String imageName;
	private String downloadURL;
	private int totalObjects;
	private String allObjects="";
	
	
	public String getAllObjects() {
		return allObjects;
	}
	public void setAllObjects(String allObjects) {
		this.allObjects += "--"+allObjects;
	}
	public int getTotalObjects() {
		return totalObjects;
	}
	public void setTotalObjects(int totalObjects) {
		this.totalObjects = totalObjects;
	}
	
	public void resetAllObjects() {
		this.allObjects="";
	}

	
	
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}




	
	
}
