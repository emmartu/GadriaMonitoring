package it.mountaineering.ring.memory.scheduled.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.util.DiskSpaceManager;
import it.mountaineering.ring.memory.util.PropertiesManager;
import it.mountaineering.ring.memory.webcam.WebcamProperty;

public class VlcLauncherScheduledTask extends TimerTask {

	private static final java.util.logging.Logger log = Logger.getLogger(VlcLauncherScheduledTask.class.getName());

	Date now;
	private boolean hasStarted = false;
	private static Map<String,File> latestFileMap;
	
	public void run() {
		log.info("run start!");
		now = new Date(); // initialize date
		this.hasStarted = true;
		initLatestFileMap();

		String[] webcamArray = null;
		webcamArray = PropertiesManager.getWebcamNames();

		String absoluteStorageFolder = PropertiesManager.getAbsoluteStorageFolder();
		Long videoLength = 0L;
		videoLength = PropertiesManager.getVideoLength();

		for (int i = 0; i < webcamArray.length; i++) {
			String webcamId = webcamArray[i];

			if(!latestFileMap.isEmpty()) {
				DiskSpaceManager.addLatestFile(latestFileMap.get(webcamArray[i]));
			}
			
			if(!DiskSpaceManager.hasEnoughMemory()) {
				DiskSpaceManager.deleteOldestFilesFromMemory();
			}

			WebcamProperty webcamProperty = null;
			webcamProperty = PropertiesManager.getWebcamPropertyById(webcamId);
			log.info("Time is :" + now+ " webcam "+webcamId+" - enabled: "+webcamProperty.isEnabled()+" - IP: "+webcamProperty.getIp()+" - folder: "+webcamProperty.getRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getRelativeStorageFolder();
			absoluteStorageFolder = checkSlashesOnPath(absoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
			String fileName = webcamId+"_"+timeStamp+".mp4";

			String storageFolderFullPath = absoluteStorageFolder+relativeStorageFolder+fileName;

			checkFolder(storageFolderFullPath);

			try {
				Runtime.
				   getRuntime().
				   exec("cmd /c start \"\" test.bat "+webcamProperty.getiD()+" "+webcamProperty.getIp()+" "+storageFolderFullPath+" "+videoLength);
				   //exec("cmd /c test.bat");
			} catch (Exception e) {
				e.printStackTrace();
			}

			latestFileMap.put(webcamId, new File(storageFolderFullPath));
		}
	}

	private static void initLatestFileMap() {
		if (latestFileMap==null) {
			latestFileMap = new HashMap<String,File>();
		}
	}

	private String checkSlashesOnPath(String folderPath) {
		log.info("check slashes end on folder path: "+folderPath);

		if (!folderPath.endsWith("\\")) {
			folderPath += "\\";
		}
		
		return folderPath;
	}

	private void checkFolder(String storageFolderFullPath) {
		log.info("check if folder: "+storageFolderFullPath+" exists");

		File directory = new File(storageFolderFullPath);
		if (!directory.exists()||!directory.isDirectory()) {
			log.info("folder doesn't exist, create");
			directory.mkdir();	
		}
	}

	public boolean isHasStarted() {
		return hasStarted;
	}

	public void setHasStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}
	
	
	public static void main(String[] args) {
		Date d = new Date();
		//STFW1_01-06-2018@12-35-16.62.mp4
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
		System.out.println(timeStamp);
	}
}
