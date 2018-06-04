package it.mountaineering.ring.memory.scheduled.task;

import java.io.File;

public class FileWithCreationTime {

	File file;
	Long creationTime;

	public FileWithCreationTime(String storageFileFullPath, long latestFileCreationTime) {
		this.file = new File(storageFileFullPath);
		this.creationTime = latestFileCreationTime;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

}
