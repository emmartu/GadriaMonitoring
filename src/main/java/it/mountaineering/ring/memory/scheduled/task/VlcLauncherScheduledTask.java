package it.mountaineering.ring.memory.scheduled.task;

import java.io.File;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.exception.CSVFormatPropertiesException;
import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.exception.WebcamPropertyIDException;
import it.mountaineering.ring.memory.main.Main;
import it.mountaineering.ring.memory.util.PropertiesManager;
import it.mountaineering.ring.memory.webcam.WebcamProperty;

public class VlcLauncherScheduledTask extends TimerTask {

	private static final java.util.logging.Logger log = Logger.getLogger(VlcLauncherScheduledTask.class.getName());

	Date now;
	private boolean hasStarted = false;
	
	public void run() {
		log.info("run start!");
		now = new Date(); // initialize date
		System.out.println("Time is :" + now); // Display current time
		this.hasStarted = true;
		
		String[] webcamArray = null;
		webcamArray = PropertiesManager.getWebcamNames();
		
		String absoluteStorageFolder = PropertiesManager.getAbsoluteStorageFolder();
		Long videoLength = 0L;
		videoLength = PropertiesManager.getVideoLength();

		for (int i = 0; i < webcamArray.length; i++) {
			WebcamProperty webcamProperty = null;
			webcamProperty = PropertiesManager.getWebcamPropertyById(webcamArray[i]);
			System.out.println("Time is :" + now+ " webcam "+webcamArray[i]+" - enabled: "+webcamProperty.isEnabled()+" - IP: "+webcamProperty.getIp()+" - folder: "+webcamProperty.getRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getRelativeStorageFolder();
			absoluteStorageFolder = checkSlashesOnPath(absoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			
			String storageFolderFullPath = absoluteStorageFolder+relativeStorageFolder;
			
			checkFolder(storageFolderFullPath);
			
			try {
				Runtime.
				   getRuntime().
				   exec("cmd /c start \"\" test.bat "+webcamProperty.getiD()+" "+webcamProperty.getIp()+" "+storageFolderFullPath+" "+videoLength);
				   //exec("cmd /c test.bat");
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		if (!directory.exists()||!directory.isDirectory()) {
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
