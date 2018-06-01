package it.mountaineering.ring.memory.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiskSpaceManager {

	public static Map<String, Long> fileMap;
	private static DiskSpaceProperties diskSpaceProperties;
	public static Long size = 0L;

	public static boolean hasEnoughMemory() {
		String storageFolder = PropertiesManager.getAbsoluteStorageFolder();
		File storageFile = new File(storageFolder);
		DiskSpaceProperties diskSpaceProperties = getDiskSpaceProperties(storageFile);

		Long safetythreshold = calculateSafetyThreshold(diskSpaceProperties);
		Long freeSpace = PropertiesManager.getDiskSpace() - diskSpaceProperties.getFolderSize();

		if (freeSpace >= safetythreshold) {
			return true;
		}

		return false;
	}

	public static void deleteOldestFilesFromMemory() {
		
		Collection<Long> unsortedEpochList = diskSpaceProperties.fileMap.keySet();
		List<Long> sorted = asSortedList(unsortedEpochList);
		Long firstItem = sorted.get(0);

		File file = diskSpaceProperties.fileMap.get(firstItem);

		diskSpaceProperties.fileMap.remove(firstItem);
		
		if (file.isFile()) {
			file.delete();
		}else{
			deleteOldestFilesFromMemory();
		}
	}

	private static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}

	protected static Long calculateSafetyThreshold(DiskSpaceProperties diskSpaceProperties) {
		Double safetythreshold = new Double(0);
		Double folderSize = new Double(diskSpaceProperties.getFolderSize());

		safetythreshold = (folderSize / diskSpaceProperties.getFileNumber());
		Double fiftyPercent = (safetythreshold / 100) * 50;

		safetythreshold = safetythreshold + fiftyPercent;

		Long longSafetythreshold = (new Double(safetythreshold)).longValue();

		return longSafetythreshold;
	}

	protected static DiskSpaceProperties getDiskSpaceProperties(File directory) {
		DiskSpaceProperties diskSpaceFile = new DiskSpaceProperties();

		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				diskSpaceFile.addFolderSize(file.length());
				diskSpaceFile.addFileNumber(1L);
				diskSpaceFile.putFileInMap(file);
			} else {
				diskSpaceFile.addFolderSize(getDiskSpaceProperties(file).getFolderSize());
				diskSpaceFile.addFileNumber(getDiskSpaceProperties(file).getFileNumber());
			}
		}

		return diskSpaceFile;
	}

	public static void addLatestFile(File file) {
		if(diskSpaceProperties==null) {
			diskSpaceProperties = new DiskSpaceProperties();
		}

		diskSpaceProperties.addFileNumber(1L);
		diskSpaceProperties.addFolderSize(file.length());
		diskSpaceProperties.putFileInMap(file);
	}

}
