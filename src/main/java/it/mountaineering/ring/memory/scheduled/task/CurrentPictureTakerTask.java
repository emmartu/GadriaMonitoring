package it.mountaineering.ring.memory.scheduled.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
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

public class CurrentPictureTakerTask extends TimerTask {

	private static final java.util.logging.Logger log = Logger.getLogger(CurrentPictureTakerTask.class.getName());
	private static DiskSpaceManager diskSPaceManager;
	
	{
		diskSPaceManager = new DiskSpaceManager(PropertiesManager.getPictureAbsoluteStorageFolder(), PropertiesManager.getPictureMaxDiskSpace());
		log.info("diskSPaceManager creation");
	}
	
	Date now;
	private boolean hasStarted = false;
	private static List<FileWithCreationTime> latestFileList;
	
	public void run() {
		log.info("run start!");
		now = new Date(); // initialize date
		this.hasStarted = true;
		initLatestFileList();

		Map<String,WebcamProperty> enabledWebcamPropertiesMap = PropertiesManager.getEnabledWebcamPropertiesMap();

		String pictureAbsoluteStorageFolder = PropertiesManager.getPictureAbsoluteStorageFolder();

		for (String webcamId : enabledWebcamPropertiesMap.keySet()){

			if(!latestFileList.isEmpty()) {
				diskSPaceManager.addLatestFile(latestFileList.get(0));
			}
			
			while(!diskSPaceManager.hasEnoughMemory()) {
				diskSPaceManager.deleteOldestFilesFromMemory();
			}

			WebcamProperty webcamProperty = enabledWebcamPropertiesMap.get(webcamId);
			log.info("Time is :" + now+ " webcam "+webcamId+" - enabled: "+webcamProperty.isEnabled()+" - IP: "+webcamProperty.getIp()+" - folder: "+webcamProperty.getPictureRelativeStorageFolder());

			String relativeStorageFolder = webcamProperty.getPictureRelativeStorageFolder();
			pictureAbsoluteStorageFolder = checkSlashesOnPath(pictureAbsoluteStorageFolder);
			relativeStorageFolder = checkSlashesOnPath(relativeStorageFolder);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss").format(new Date());
			String fileName = webcamId+"_"+timeStamp+".jpg";

			String storageFolderFullPath = pictureAbsoluteStorageFolder + relativeStorageFolder;
			checkFolder(storageFolderFullPath);

			String storageFileFullPath = pictureAbsoluteStorageFolder + relativeStorageFolder + fileName;

			long latestFileCreationTime = System.currentTimeMillis();
			
			String imageUrl = "http://"+webcamProperty.getIp()+"/record/current.jpg";
			
			try {
				saveImage(imageUrl, storageFileFullPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			FileWithCreationTime fileWithCreationTime = new FileWithCreationTime(storageFileFullPath, latestFileCreationTime);
			latestFileList.add(fileWithCreationTime);
		}
	}

	private void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

	private static void initLatestFileList() {
		if (latestFileList==null) {
			latestFileList = new ArrayList<FileWithCreationTime>();
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
}