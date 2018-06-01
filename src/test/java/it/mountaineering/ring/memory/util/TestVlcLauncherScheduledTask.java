package it.mountaineering.ring.memory.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.util.DiskSpaceManager;
import it.mountaineering.ring.memory.webcam.WebcamProperty;

public class TestVlcLauncherScheduledTask {

	Date now;
	private boolean hasStarted = false;
	private static Map<String, File> latestFileMap;
	private static final String resourceFolder = "C:\\Users\\Lele\\workspace\\CircularMemory\\src\\test\\resources\\";

	
	public void test() {
		String configFile = "src/test/resources/configRunTest.properties";
		PropertiesManager.setConfigFile(configFile);
		PropertiesManager.checkIp = false;

		try {
			PropertiesManager.setupConfigProperties();
		} catch (PropertiesException e) {
			e.printStackTrace();
		}		

		now = new Date(); // initialize date
		this.hasStarted = true;
		initLatestFileMap();

		String[] webcamArray = null;
		webcamArray = PropertiesManager.getWebcamNames();

		String absoluteStorageFolder = PropertiesManager.getAbsoluteStorageFolder();

		for (int i = 0; i < webcamArray.length; i++) {
			String webcamId = webcamArray[i];

			if (!latestFileMap.isEmpty()) {
				DiskSpaceManager.addLatestFile(latestFileMap.get(webcamArray[i]));
			}

			if (!DiskSpaceManager.hasEnoughMemory()) {
				DiskSpaceManager.deleteOldestFilesFromMemory();
			}

			WebcamProperty webcamProperty = null;
			webcamProperty = PropertiesManager.getWebcamPropertyById(webcamId);
			System.out.println("Time is :" + now + " webcam " + webcamId + " - enabled: " + webcamProperty.isEnabled() + " - IP: "
					+ webcamProperty.getIp() + " - folder: " + webcamProperty.getRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getRelativeStorageFolder();
			absoluteStorageFolder = checkSlashesOnPath(absoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
			String fileName = webcamId + "_" + timeStamp + ".mp4";

			String storageFolderFullPath = absoluteStorageFolder + relativeStorageFolder + fileName;

			checkFolder(storageFolderFullPath);

			try {
				UtilityForTests.createNewFile(storageFolderFullPath);
				UtilityForTests.copyFileUsingFileStreams(new File(resourceFolder+"test.mp4"),new File(storageFolderFullPath));
			} catch (Exception e) {
				e.printStackTrace();
			}

			latestFileMap.put(webcamId, new File(storageFolderFullPath));
		}
	}

	private static void initLatestFileMap() {
		if (latestFileMap == null) {
			latestFileMap = new HashMap<String, File>();
		}
	}

	private String checkSlashesOnPath(String folderPath) {
		if (!folderPath.endsWith("\\")) {
			folderPath += "\\";
		}

		return folderPath;
	}

	private void checkFolder(String storageFolderFullPath) {
		File directory = new File(storageFolderFullPath);
		if (!directory.exists() || !directory.isDirectory()) {
			directory.mkdir();
		}
	}

	public boolean isHasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

}