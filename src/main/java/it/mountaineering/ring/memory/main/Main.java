package it.mountaineering.ring.memory.main;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.scheduled.task.VlcLauncherScheduledTask;
import it.mountaineering.ring.memory.util.PropertiesManager;


public class Main {

	private static final java.util.logging.Logger log = Logger.getLogger(Main.class.getName());
	private static final long MIN_SPACE = 1000;

	public static Timer timer;
	public static VlcLauncherScheduledTask vlcLauncher;
	
	PropertiesManager prop = new PropertiesManager();
	OutputStream output = null;

	public static void main(String[] args) {
		Main main = new Main();

		try {
			main.setupProperties();
		} catch (PropertiesException e) {
			log.info(e.getMessage());
			log.info("the application has been stopped");
			return;
		}
		
		main.launchScheduledTasks();

		//try {
		//	Runtime.
		//	   getRuntime().
		//	   exec("cmd /c test.bat");
		//} catch (IOException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}

	}
	
	private void setupProperties() throws PropertiesException {
		log.info("*******************************************");
		log.info("*******************************************");
		log.info("****** WEBCAM VIDEO RECORDER MANAGER ******");
		log.info("*******************************************");
		log.info("*******************************************");

		PropertiesManager.setupConfigProperties();
	}


	private void launchScheduledTasks() {
		Long videoLength = 0L;
		videoLength = PropertiesManager.getVideoLength();
		Long overlap = PropertiesManager.getOverlap();
		
		Long taskTimePeriod = videoLength-overlap;
		Long millisTaskTimePeriod = 1000*taskTimePeriod;
		timer = new Timer();
		vlcLauncher = new VlcLauncherScheduledTask();
		timer.schedule(vlcLauncher, 0, millisTaskTimePeriod);
	}

	/*
	timer.cancel();
	timer.purge();
	 */

	public boolean isDiskSpaceAvailable() {
		long space = getFolderSpace();

		if (space >= MIN_SPACE) {
			return true;
		}

		return false;
	}

	public long getFolderSpace() {
		long space = 0L;
		String folderName = PropertiesManager.getAbsoluteStorageFolder();
		//String folderName = "C:\\Users\\Lele\\Documents\\LavoroWebCamMobotix\\TEST_FOLDER";
		File fl = new File(folderName);
		if (fl.exists()) {
			if (fl.isDirectory()) {
				//space = fl.listFiles(filter)
			}
		}

		return space;
	}

	public Date folderCreatedDate(String folderName) {
		long date = 0;
		File fl = new File(folderName);
		if (fl.isDirectory())
			if (fl.exists())
				date = fl.lastModified();
		return new Date(date);
	}
}
