package it.mountaineering.ring.memory.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import it.mountaineering.ring.memory.bean.WebcamProperty;
import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.util.DiskSpaceManager;

public class TestVlcLauncherScheduledTask {

	Date now;
	private static List<File> latestFileList;
	private static final String resourceFolder = "C:\\Users\\Lele\\workspace\\CircularMemory\\src\\test\\resources\\";
	private static DiskSpaceManager diskSPaceManager;
	
	{
		diskSPaceManager = new DiskSpaceManager();
	}


	@Test
	public void runTest() {
		String configFile = "src/test/resources/configRunTest.properties";
		PropertiesManager.setConfigFile(configFile);
		PropertiesManager.checkIp = false;

		try {
			PropertiesManager.setupConfigProperties();
		} catch (PropertiesException e) {
			e.printStackTrace();
		}		

		for(int a=0;a<=4;a++) {
			System.out.println("runTest num = "+a);
			test();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		assertTrue(1==1);
	}

	public void test() {
		now = new Date(); // initialize date
		initLatestFileMap();

		String[] webcamArray = null;
		webcamArray = PropertiesManager.getWebcamNames();

		String absoluteStorageFolder = PropertiesManager.getVideoAbsoluteStorageFolder();

		for (int i = 0; i < webcamArray.length; i++) {
			String webcamId = webcamArray[i];

			if (!latestFileList.isEmpty()) {
				System.out.println("addLatestFile to DiskSpaceManager: "+latestFileList.get(0).getName());
				//DiskSpaceManager.addLatestFile(latestFileList.get(0));
				latestFileList.remove(0);
			}

			if (!diskSPaceManager.hasEnoughMemory(PropertiesManager.getVideoAbsoluteStorageFolder())) {
				System.out.println("!DiskSpaceManager.hasEnoughMemory(): deleteOldestFilesFromMemory()");
				diskSPaceManager.deleteOldestFilesFromMemory();
			}

			WebcamProperty webcamProperty = null;
			webcamProperty = PropertiesManager.getWebcamPropertyById(webcamId);
			System.out.println("Time is :" + now + " webcam " + webcamId + " - enabled: " + webcamProperty.isEnabled() + " - IP: "
					+ webcamProperty.getIp() + " - folder: " + webcamProperty.getVideoRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getVideoRelativeStorageFolder();
			absoluteStorageFolder = checkSlashesOnPath(absoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
			String fileName = webcamId + "_" + timeStamp + ".mp4";

			String storageFolderFullPath = absoluteStorageFolder + relativeStorageFolder;
			checkFolder(storageFolderFullPath);

			String storageFileFullPath = absoluteStorageFolder + relativeStorageFolder + fileName;

			try {
				UtilityForTests.createNewFile(storageFileFullPath);
				UtilityForTests.copyFileUsingFileStreams(new File(resourceFolder+"test.mp4"),new File(storageFileFullPath));
			} catch (Exception e) {
				e.printStackTrace();
			}

			latestFileList.add(new File(storageFileFullPath));
		}
		
	}

	private static void initLatestFileMap() {
		if (latestFileList == null) {
			latestFileList = new ArrayList<File>();
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

}