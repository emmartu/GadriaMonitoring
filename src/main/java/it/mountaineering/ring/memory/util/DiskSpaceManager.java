package it.mountaineering.ring.memory.util;

import java.io.File;
import java.util.Map;

public class DiskSpaceManager {

	public static Map<String,Long> fileMap;
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
		if (fileMap.isEmpty()&&size==0){
			PopulateFileMap();
		}
		
		
		
		return false;
	}

	private static void PopulateFileMap() {
	    
		long length = 0;
	    String directoryPath = PropertiesManager.getStorageFolder();
	    File directory = new File(directoryPath);
	    
		for (File file : directory.listFiles()) {
	        if (file.isFile()) {
	        	fileMap.put(file.getName(), file.length());
	            length += file.length();
	        }
	    }
		
	    size =  length;
	}
}
