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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((iD == null) ? 0 : iD.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((relativeStorageFolder == null) ? 0 : relativeStorageFolder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WebcamProperty other = (WebcamProperty) obj;
		if (enabled != other.enabled)
			return false;
		if (iD == null) {
			if (other.iD != null)
				return false;
		} else if (!iD.equals(other.iD))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (relativeStorageFolder == null) {
			if (other.relativeStorageFolder != null)
				return false;
		} else if (!relativeStorageFolder.equals(other.relativeStorageFolder))
			return false;
		return true;
	}

}
