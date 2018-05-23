package it.mountaineering.ring.memory.main;

import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.util.DiskSpaceManager;
import it.mountaineering.ring.memory.util.PropertiesManager;


public class Main {

	private static final java.util.logging.Logger log = Logger.getLogger(Main.class.getName());
	private static final long MIN_SPACE = 1000;
	
	Properties prop = new Properties();
	OutputStream output = null;

	public static void main(String[] args) {
		Main main = new Main();
		
		System.out.println(PropertiesManager.getStorageFolder());
		long space = main.getFolderSpace();
		
		System.out.println("space = "+space);
		
		if (DiskSpaceManager.isDiskSpaceAvailable()) {
			main.launchVlcRecord();
		}else {
			main.freeDiskSpace();
		}

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
		String folderName = "C:\\Users\\Lele\\Documents\\LavoroWebCamMobotix\\TEST_FOLDER";//ResourceLocator.getStorageFolder();
		File fl = new File(folderName);
		if (fl.exists()) {
			if (fl.isDirectory()) {
				space = fl.listFiles(filter)
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
