package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;

import it.mountaineering.ring.memory.scheduled.task.FileWithCreationTime;

public class DiskSpaceProperties {


	Long folderSize;
	Long fileNumber;
	Map<Long,File> fileMap;
	
	public DiskSpaceProperties() {
		folderSize = 0L;
		fileNumber = 0L;
		fileMap = new LinkedHashMap<Long,File>();
	}

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

	public void putFileInMap(FileWithCreationTime fileWithCreationTime) {
		if(fileMap==null) {
			fileMap = new LinkedHashMap<Long, File>();
		}
		
		fileMap.put(fileWithCreationTime.getCreationTime(), fileWithCreationTime.getFile());
	}
	
	private long getFileCreationEpoch(File file) {
		try {
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			return attr.creationTime().toInstant().toEpochMilli();
		} catch (IOException e) {
			throw new RuntimeException(file.getAbsolutePath(), e);
		}
	}

	public Map<Long,File> getFileMap() {
		return fileMap;
	}

	public void mergeFileMap(Map<Long, File> fileMapToMerge) {
		if(fileMap==null) {
			fileMap = new LinkedHashMap<Long, File>();
		}

		fileMap.putAll(fileMapToMerge);
	}
}
