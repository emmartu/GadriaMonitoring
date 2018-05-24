package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class DiskSpaceManager {

	public static Map<String, Long> fileMap;
	public static Long size = 0L;

	private long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

	public static boolean isDiskSpaceAvailable() {
		if (size == 0) {
			PopulateFileMap();
		}

		long diskSpaceAvailable = PropertiesManager.getDiskSpace() - size;

		if (diskSpaceAvailable < PropertiesManager.getMinDiskSpace()) {
			return false;
		}

		return true;
	}

	private static void PopulateFileMap() {

		long length = 0;
		String storageFolder = PropertiesManager.getStorageFolder();
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
}
