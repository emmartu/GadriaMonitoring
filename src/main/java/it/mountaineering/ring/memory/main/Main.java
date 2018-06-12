package it.mountaineering.ring.memory.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.scheduled.task.CurrentPictureTakerTask;
import it.mountaineering.ring.memory.scheduled.task.VlcLauncherScheduledTask;
import it.mountaineering.ring.memory.util.PropertiesManager;


public class Main {

	private static final java.util.logging.Logger log = Logger.getLogger(Main.class.getName());

	private static final String LOGGING_PROPERTIES = "logging.properties";

	public static Timer vlcTimer;
	public static VlcLauncherScheduledTask vlcLauncher;

	public static Timer pictureTakerTimer;
	public static CurrentPictureTakerTask pictureTakerLauncher;

	PropertiesManager prop = new PropertiesManager();
	OutputStream output = null;

	public static void main(String[] args) {
		Main main = new Main();
		main.setUpLogger();
		try {
			main.setupProperties();
		} catch (PropertiesException e) {
			log.severe(e.getMessage()+" **** the application has been stopped *** ");
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
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(LOGGING_PROPERTIES));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
        
	}
	
	private void setupProperties() throws PropertiesException {
		log.info("*******************************************");
		log.info("*******************************************");
		log.info("****** WEBCAM VIDEO RECORDER MANAGER ******");
		log.info("*******************************************");
		log.info("*******************************************");

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
