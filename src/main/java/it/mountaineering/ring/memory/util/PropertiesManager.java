package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesManager {

	static Properties prop = new Properties();
	static InputStream input = null;

	static {
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getStorageFolder() {
		String storageFolder = prop.getProperty("StorageFolder");
		
		return storageFolder;
	}

	public static Long getDiskSpace() {
		long diskSpace = Long.parseLong(prop.getProperty("DiskSpace"));

		return diskSpace;
	}

	public static Long getMinDiskSpace() {
		long minDiskSpace = Long.parseLong(prop.getProperty("MinDiskSpace"));

		return minDiskSpace;
	}

	public static String[] getWebcamArray() {
		String[] webcamArray = prop.getProperty("WebCams").split(",");
		
		return webcamArray;
	}
	
	public static Long getVideoLength() {
		long videoLength = Long.parseLong(prop.getProperty("VideoLength"));

		return videoLength;
	}
	
	public static Long getOverlap() {
		long overlap = Long.parseLong(prop.getProperty("Overlap"));

		return overlap;
	}
}