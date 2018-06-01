package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;

public class DiskSpaceProperties {

	Long folderSize = 0L;
	Long fileNumber = 0L;
	Map<Long,File> fileMap;

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

	public void putFileInMap(File file) {
		if(fileMap==null) {
			fileMap = new LinkedHashMap<Long, File>();
		}
		
		fileMap.put(getFileCreationEpoch(file), file);
	}
	
	private long getFileCreationEpoch(File file) {
		try {
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			return attr.creationTime().toInstant().toEpochMilli();
		} catch (IOException e) {
			throw new RuntimeException(file.getAbsolutePath(), e);
		}
	}

}
