package it.mountaineering.ring.memory.main;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.scheduled.task.VlcLauncherScheduledTask;
import it.mountaineering.ring.memory.util.PropertiesManager;


public class Main {

	private static final java.util.logging.Logger log = Logger.getLogger(Main.class.getName());
	private static final long MIN_SPACE = 1000;
	
	Properties prop = new Properties();
	OutputStream output = null;

	public static void main(String[] args) {
		Main main = new Main();
		
		main.launchScheduledTasks();
		
		try {
			Runtime.
			   getRuntime().
			   exec("cmd /c test.bat");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(PropertiesManager.getStorageFolder());
		//long space = main.getFolderSpace();
		
		//System.out.println("space = "+space);
		
		//if (DiskSpaceManager.isDiskSpaceAvailable()) {
		//	main.launchVlcRecord();
		//}else {
		//	main.freeDiskSpace();
		//}

	}
	
	
	private void launchScheduledTasks() {
		Long videoLength = PropertiesManager.getVideoLength();
		Long overlap = PropertiesManager.getOverlap();
		
		Long taskTimePeriod = videoLength-overlap;
		Timer time = new Timer();
		VlcLauncherScheduledTask vlcLauncher = new VlcLauncherScheduledTask();
		time.schedule(vlcLauncher, 0, taskTimePeriod);

		//for demo only.
		//for (int i = 0; i <= 5; i++) {
		//	System.out.println("Execution in Main Thread...." + i);
		//	try {
		//		Thread.sleep(2000);
		//	} catch (InterruptedException e) {
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		//	if (i == 5) {
		//		System.out.println("Application Terminates");
		//		System.exit(0);
		//	}
		//}		
	}


	public void freeDiskSpace() {
		// TODO Auto-generated method stub

	}

	public void launchVlcRecord() {
		// TODO Auto-generated method stub

	}

	public boolean isDiskSpaceAvailable() {
		long space = getFolderSpace();

		if (space >= MIN_SPACE) {
			return true;
		}

		return false;
	}

	public long getFolderSpace() {
		long space = 0L;
		String folderName = PropertiesManager.getStorageFolder();
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
