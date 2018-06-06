package it.mountaineering.ring.memory.main;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.scheduled.task.CurrentPictureTakerTask;
import it.mountaineering.ring.memory.scheduled.task.VlcLauncherScheduledTask;
import it.mountaineering.ring.memory.util.PropertiesManager;


public class Main {

	private static final java.util.logging.Logger log = Logger.getLogger(Main.class.getName());

	public static Timer vlcTimer;
	public static VlcLauncherScheduledTask vlcLauncher;

	public static Timer pictureTakerTimer;
	public static CurrentPictureTakerTask pictureTakerLauncher;

	PropertiesManager prop = new PropertiesManager();
	OutputStream output = null;

	public static void main(String[] args) {
		Main main = new Main();
		//main.setUpLogger();
		try {
			main.setupProperties();
		} catch (PropertiesException e) {
			log.info(e.getMessage());
			log.info("the application has been stopped");
			return;
		}

		if(PropertiesManager.isPictureCaptureEnabled()) {
			main.launchPictureTakerScheduledTasks();			
		}
		
		if(PropertiesManager.isVideoCaptureEnabled()) {
			main.launchVlcScheduledTasks();			
		}
	}
	
	private void setUpLogger() {
	    FileHandler fileHandler = null;

	    try {
			fileHandler = new FileHandler(".\\MyLogFile.log", true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    log.addHandler(fileHandler);
	    log.setLevel(Level.ALL);
	    SimpleFormatter formatter = new SimpleFormatter();
	    fileHandler.setFormatter(formatter);

	    log.log(Level.WARNING, "My first log");
	}
	
	private void setupProperties() throws PropertiesException {
		//log.info("*******************************************");
		//log.info("*******************************************");
		//log.info("****** WEBCAM VIDEO RECORDER MANAGER ******");
		//log.info("*******************************************");
		//log.info("*******************************************");

		System.out.println("*******************************************");
		System.out.println("*******************************************");
		System.out.println("****** WEBCAM VIDEO RECORDER MANAGER ******");
		System.out.println("*******************************************");
		System.out.println("*******************************************");

		PropertiesManager.setupConfigProperties();
	}


	private void launchVlcScheduledTasks() {
		Long videoLength = 0L;
		videoLength = PropertiesManager.getVideoLength();
		Long overlap = PropertiesManager.getOverlap();
		
		Long taskTimePeriod = videoLength-overlap;
		Long millisTaskTimePeriod = 1000*taskTimePeriod;
		vlcTimer = new Timer();
		vlcLauncher = new VlcLauncherScheduledTask();
		vlcTimer.schedule(vlcLauncher, 0, millisTaskTimePeriod);
	}
	
	private void launchPictureTakerScheduledTasks() {
		Long pictureInterval = 0L;
		pictureInterval = PropertiesManager.getPictureInterval();
		
		Long millisTaskTimePeriod = 1000*pictureInterval;
		pictureTakerTimer = new Timer();
		pictureTakerLauncher = new CurrentPictureTakerTask();
		pictureTakerTimer.schedule(pictureTakerLauncher, 0, millisTaskTimePeriod);
	}

}
