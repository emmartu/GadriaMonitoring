package it.mountaineering.ring.memory.scheduled.task;

import java.util.Date;
import java.util.TimerTask;

import it.mountaineering.ring.memory.util.PropertiesManager;
import it.mountaineering.ring.memory.webcam.WebcamProperty;

public class VlcLauncherScheduledTask extends TimerTask {


	Date now;


	public void run() {
		now = new Date(); // initialize date
		System.out.println("Time is :" + now); // Display current time

		String[] webcamArray = PropertiesManager.getWebcamArray();
		
		for (int i = 0; i < webcamArray.length; i++) {
			WebcamProperty webcamProperty = PropertiesManager.getWebcamPropertyByName(webcamArray[i]);
			System.out.println("Time is :" + now); // Display current time
		}

	}
}
