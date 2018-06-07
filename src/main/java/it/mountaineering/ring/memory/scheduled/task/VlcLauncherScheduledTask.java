package it.mountaineering.ring.memory.scheduled.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.bean.FileWithCreationTime;
import it.mountaineering.ring.memory.bean.WebcamProperty;
import it.mountaineering.ring.memory.util.DiskSpaceManager;
import it.mountaineering.ring.memory.util.PropertiesManager;

public class VlcLauncherScheduledTask extends TimerTask {

	private static final java.util.logging.Logger log = Logger.getLogger(VlcLauncherScheduledTask.class.getName());
	private static final String VLC_VIDEO_RECORDER_BAT = "VlcVideoRecorder.bat";
	private static DiskSpaceManager diskSPaceManager;
	
	{
		diskSPaceManager = new DiskSpaceManager(PropertiesManager.getVideoAbsoluteStorageFolder(), PropertiesManager.getVideoMaxDiskSpace());
		checkMemory();		
	}

	Date now;
	private boolean hasStarted = false;
	private static List<FileWithCreationTime> latestFileList;
	
	public void run() {
		//log.info("run start!");
		now = new Date(); // initialize date
		System.out.println("VlcLauncherScheduledTask run date: "+now);
		this.hasStarted = true;
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
			//log.info("Time is :" + now+ " webcam "+webcamId+" - enabled: "+webcamProperty.isEnabled()+" - IP: "+webcamProperty.getIp()+" - folder: "+webcamProperty.getVideoRelativeStorageFolder());
			System.out.println("Time is :" + now+ " webcam "+webcamId+" - enabled: "+webcamProperty.isEnabled()+" - IP: "+webcamProperty.getIp()+" - folder: "+webcamProperty.getVideoRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getVideoRelativeStorageFolder();
			absoluteStorageFolder = checkSlashesOnPath(absoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
			String fileName = webcamId+"_"+timeStamp+".mp4";

			String storageFolderFullPath = absoluteStorageFolder + relativeStorageFolder;
			checkFolder(storageFolderFullPath);

			String storageFileFullPath = absoluteStorageFolder + relativeStorageFolder + fileName;

			long latestFileCreationTime = System.currentTimeMillis();
			
			String videoLanExePath = PropertiesManager.getVideoLanExePath();
			//log.info("videoLanExePath: "+videoLanExePath);
			
			try {
				Runtime.
				   getRuntime().
				   exec("cmd /c start /B \"\" "+VLC_VIDEO_RECORDER_BAT+" "+webcamProperty.getiD()+" "+webcamProperty.getIp()+" "+storageFileFullPath+" "+videoLength+" \""+videoLanExePath+"\"");
			} catch (Exception e) {
				System.out.println("exception occurred grabbing video");
				e.printStackTrace();
			}

			FileWithCreationTime fileWithCreationTime = new FileWithCreationTime(storageFileFullPath, latestFileCreationTime);
			latestFileList.add(fileWithCreationTime);
		}
	}

	private void checkMemory() {
		while(!diskSPaceManager.hasEnoughMemory()) {
			diskSPaceManager.deleteOldestFilesFromMemory();
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

	private void checkFolder(String storageFolderFullPath) {
		File directory = new File(storageFolderFullPath);
		if (!directory.exists()||!directory.isDirectory()) {
			System.out.println("folder: "+directory+" doesn't exist, create");
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
