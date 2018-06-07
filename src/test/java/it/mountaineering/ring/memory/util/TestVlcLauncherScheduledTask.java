package it.mountaineering.ring.memory.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.junit.Test;

import it.mountaineering.ring.memory.bean.FileWithCreationTime;
import it.mountaineering.ring.memory.bean.WebcamProperty;
import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.scheduled.task.VlcLauncherScheduledTask;
import it.mountaineering.ring.memory.util.DiskSpaceManager;

public class TestVlcLauncherScheduledTask {

	private DiskSpaceManager diskSPaceManager;
	
	Date now;
	private boolean hasStarted = false;
	private static List<FileWithCreationTime> latestFileList;
	private final String resourceFolder = "C:\\Users\\Lele\\workspace\\CircularMemory\\src\\test\\resources\\";
	
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

		diskSPaceManager = new DiskSpaceManager(PropertiesManager.getVideoAbsoluteStorageFolder(), PropertiesManager.getVideoMaxDiskSpace());
		checkMemory();		

		//System.out.println("runTest num");
		//test();

		for(int a=0;a<=1;a++) {
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
		now = new Date();
		initLatestFileList();

		Map<String,WebcamProperty> enabledWebcamPropertiesMap = PropertiesManager.getEnabledWebcamPropertiesMap();

		String absoluteStorageFolder = PropertiesManager.getVideoAbsoluteStorageFolder();
		Long videoLength = 0L;
		videoLength = PropertiesManager.getVideoLength();

		for (String webcamId : enabledWebcamPropertiesMap.keySet()){
			if(latestFileList.size()==2) {
				FileWithCreationTime fileWithCreationTime = latestFileList.remove(0);
				diskSPaceManager.addLatestFile(fileWithCreationTime);

				checkMemory();
			}

			WebcamProperty webcamProperty = enabledWebcamPropertiesMap.get(webcamId);
			System.out.println("Time is :" + now + " webcam " + webcamId + " - enabled: " + webcamProperty.isEnabled() + " - IP: "
					+ webcamProperty.getIp() + " - folder: " + webcamProperty.getVideoRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getVideoRelativeStorageFolder();
			absoluteStorageFolder = checkSlashesOnPath(absoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
			String fileName = webcamId+"_"+timeStamp+".mp4";

			String storageFolderFullPath = absoluteStorageFolder + relativeStorageFolder;
			checkFolder(storageFolderFullPath);

			String storageFileFullPath = absoluteStorageFolder + relativeStorageFolder + fileName;
			
			long latestFileCreationTime = System.currentTimeMillis();

			try {
				UtilityForTests.createNewFile(storageFileFullPath);
				UtilityForTests.copyFileUsingFileStreams(new File(resourceFolder+"test.mp4"),new File(storageFileFullPath));
			} catch (Exception e) {
				e.printStackTrace();
			}

			FileWithCreationTime fileWithCreationTime = new FileWithCreationTime(storageFileFullPath, latestFileCreationTime);
			latestFileList.add(fileWithCreationTime);
		}
		
	}

	private static void initLatestFileList() {
		if (latestFileList==null) {
			latestFileList = new ArrayList<FileWithCreationTime>();
		}
	}

	private String checkSlashesOnPath(String folderPath) {
		if (!folderPath.endsWith("\\")) {
			folderPath += "\\";
		}

		return folderPath;
	}

	private void checkMemory() {
		while(!diskSPaceManager.hasEnoughMemory()) {
			diskSPaceManager.deleteOldestFilesFromMemory();
		}
	}

	private void checkFolder(String storageFolderFullPath) {
		File directory = new File(storageFolderFullPath);
		if (!directory.exists() || !directory.isDirectory()) {
			directory.mkdir();
		}
	}

}