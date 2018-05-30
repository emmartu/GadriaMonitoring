package it.mountaineering.ring.memory.util;

public class DiskSpaceFile {

	Long folderSize = 0L;
	Long fileNumber = 0L;

	public Long getFolderSize() {
		return folderSize;
	}

	public void setFolderSize(Long folderSize) {
		this.folderSize = folderSize;
	}

	public Long getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Long fileNumber) {
		this.fileNumber = fileNumber;
	}

	public void addFolderSize(long length) {
		this.folderSize += length;		
	}

	public void addFileNumber(Long fileCount) {
		this.fileNumber += fileCount;		
	}

}
