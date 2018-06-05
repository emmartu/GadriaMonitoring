package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.bean.DiskSpaceProperties;
import it.mountaineering.ring.memory.bean.FileWithCreationTime;

public class DiskSpaceManager {

	private static final java.util.logging.Logger log = Logger.getLogger(DiskSpaceManager.class.getName());
	private DiskSpaceProperties diskSpaceProperties;
	public Long size = 0L;
	
	public boolean hasEnoughMemory(String storageFolder) {
		log.info("hasEnoughMemory()");
		File storageFile = new File(storageFolder);

		if(diskSpaceProperties==null||diskSpaceProperties.getFileNumber()==0L) {
			diskSpaceProperties = getDiskSpaceProperties(storageFile);
		}

		Long safetythreshold = calculateSafetyThreshold(diskSpaceProperties);
		Long freeSpace = PropertiesManager.getVideoMaxDiskSpace() - diskSpaceProperties.getFolderSize();

		if (freeSpace >= safetythreshold) {
			log.info("freeSpace >= safetythreshold");
			return true;
		}

		return false;
	}

	public void deleteOldestFilesFromMemory() {
		
		Collection<Long> unsortedEpochList = diskSpaceProperties.getFileMap().keySet();
		List<Long> sorted = asSortedList(unsortedEpochList);
		Long firstItem = sorted.get(0);

		File file = diskSpaceProperties.getFileMap().get(firstItem);

		diskSpaceProperties.getFileMap().remove(firstItem);
		
		if (file.isFile()) {
			file.delete();
		}else{
			deleteOldestFilesFromMemory();
		}
	}

	private <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}

	protected Long calculateSafetyThreshold(DiskSpaceProperties diskSpaceProperties) {
		Double safetythreshold = new Double(0);
		Double folderSize = new Double(diskSpaceProperties.getFolderSize());

		safetythreshold = (folderSize / diskSpaceProperties.getFileNumber());
		Double fiftyPercent = (safetythreshold / 100) * 50;

		safetythreshold = safetythreshold + fiftyPercent;

		Long longSafetythreshold = (new Double(safetythreshold)).longValue();

		return longSafetythreshold;
	}

	
	
	protected DiskSpaceProperties getDiskSpaceProperties(File directory) {
		log.info("init DiskSpaceProperties getDiskSpaceProperties()");

		DiskSpaceProperties diskSpaceFile = new DiskSpaceProperties();

		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				diskSpaceFile.addFolderSize(file.length());
				diskSpaceFile.addFileNumber(1L);
				FileWithCreationTime fileWithCreationTime = new FileWithCreationTime(file.getAbsolutePath(), getFileCreationEpoch(file));
				diskSpaceFile.putFileInMap(fileWithCreationTime);
			} else {
				DiskSpaceProperties diskSpaceFileTemp = getDiskSpaceProperties(file);
				diskSpaceFile.addFolderSize(diskSpaceFileTemp.getFolderSize());
				diskSpaceFile.addFileNumber(diskSpaceFileTemp.getFileNumber());
				diskSpaceFile.mergeFileMap(diskSpaceFileTemp.getFileMap());
			}
		}

		return diskSpaceFile;
	}

	public void addLatestFile(FileWithCreationTime fileWithCreationTime) {
		log.info("addLatestFile("+fileWithCreationTime.getFile().getName()+")");
		if(diskSpaceProperties==null) {
			diskSpaceProperties = new DiskSpaceProperties();
		}

		diskSpaceProperties.addFileNumber(1L);
		diskSpaceProperties.addFolderSize(fileWithCreationTime.getFile().length());
		diskSpaceProperties.putFileInMap(fileWithCreationTime);
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
