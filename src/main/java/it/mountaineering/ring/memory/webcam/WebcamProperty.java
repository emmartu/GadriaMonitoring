package it.mountaineering.ring.memory.webcam;

public class WebcamProperty {

	String iD;
	boolean enabled;
	String ip;
	String relativeStorageFolder;

	public String getiD() {
		return iD;
	}

	public void setiD(String iD) {
		this.iD = iD;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRelativeStorageFolder() {
		return relativeStorageFolder;
	}

	public void setRelativeStorageFolder(String relativeStorageFolder) {
		this.relativeStorageFolder = relativeStorageFolder;
	}

}
