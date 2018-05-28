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
		try {
			webcamArray = PropertiesManager.getWebcamArray();
		} catch (CSVFormatPropertiesException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		String absoluteStorageFolder = PropertiesManager.getAbsoluteStorageFolder();
		Long videoLength = 0L;
		try {
			videoLength = PropertiesManager.getVideoLength();
		} catch (PropertiesException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < webcamArray.length; i++) {
			WebcamProperty webcamProperty = null;
			try {
				webcamProperty = PropertiesManager.getWebcamPropertyById(webcamArray[i]);
			} catch (WebcamPropertyIDException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
