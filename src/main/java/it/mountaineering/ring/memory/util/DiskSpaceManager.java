package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class DiskSpaceManager {

	public static Map<String, Long> fileMap;
	public static Long size = 0L;

	
	public static boolean isMemoryEnough() {
		String storageFolder = PropertiesManager.getAbsoluteStorageFolder();
		File storageFile = new File(storageFolder);
		DiskSpaceFile diskSpaceFile = folderSize(storageFile);

		Long safetythreshold = calculateSafetyThreshold(diskSpaceFile);
		Long freeSpace = PropertiesManager.getDiskSpace() - diskSpaceFile.getFolderSize();
		
		if(freeSpace>=safetythreshold) {
			return true;
		}
		
		return false;
	}

	
	private static Long calculateSafetyThreshold(DiskSpaceFile diskSpaceFile) {
		Long safetythreshold = 0L;
		
		int enabledWebcam = PropertiesManager.getEnabledWebcam().keySet().size();
		safetythreshold = (diskSpaceFile.getFolderSize() / diskSpaceFile.getFileNumber())*enabledWebcam;
		Long fiftyPercent = (safetythreshold/100)*50;

		safetythreshold = safetythreshold + fiftyPercent;
		
		return safetythreshold;
	}


	private static DiskSpaceFile folderSize(File directory) {
		long length = 0;
		long fileCounter = 0L;
		DiskSpaceFile diskSpaceFile = new DiskSpaceFile();

		String[] webcamArray = PropertiesManager.getWebcamNames();
		String absoluteStorageFolder = PropertiesManager.getAbsoluteStorageFolder();
		
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				diskSpaceFile.addFolderSize(file.length());
				diskSpaceFile.addFileNumber(1L);
			}
			else {
				diskSpaceFile.addFolderSize(folderSize(file).getFolderSize());
				diskSpaceFile.addFileNumber(folderSize(file).getFileNumber());
			}
		}
		
		return diskSpaceFile;
	}

		
	public static boolean isDiskSpaceAvailable() {
		if (size == 0) {
			PopulateFileMap();
		}

		long diskSpaceAvailable = PropertiesManager.getDiskSpace() - size;

		/*if (diskSpaceAvailable < PropertiesManager.getDiskSpace()) {
			return false;
		}*/

		return true;
	}

	private static void PopulateFileMap() {

		long length = 0;
		String storageFolder = PropertiesManager.getAbsoluteStorageFolder();
		String directoryPath = storageFolder.replace("\\", "\\\\");

		File directory = new File(storageFolder);
		if (directory.exists()) {
			if (directory.isDirectory()) {

				for (File file : directory.listFiles()) {
					if (file.isFile()) {
						// fileMap.put(file.getName(), file.length());
						length += file.length();
					}
				}

			}
		}
		
		size = length;
	}

	
	public static void main(String[] args) {
		
	}
}
