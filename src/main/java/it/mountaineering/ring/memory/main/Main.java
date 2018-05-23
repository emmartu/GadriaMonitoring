package it.mountaineering.ring.memory.main;

import java.io.File;
import java.util.Date;

public class Main {

	private static final long MIN_SPACE = 1000;

	public static void main(String[] args) {
		Main main = new Main();
		if (main.isDiskSpaceAvailable()) {
			main.launchVlcRecord();
		}else {
			main.freeDiskSpace();
		}

	}

	private void freeDiskSpace() {
		// TODO Auto-generated method stub

	}

	private void launchVlcRecord() {
		// TODO Auto-generated method stub

	}

	private boolean isDiskSpaceAvailable() {
		long space = getFolderSpace();

		if (space >= MIN_SPACE) {
			return true;
		}

		return false;
	}

	private long getFolderSpace() {
		long space = 0L;
		String folderName = "C:\\Users\\Lele\\Documents\\LavoroWebCamMobotix\\TEST_FOLDER";
		File fl = new File(folderName);
		if (fl.isDirectory()) {
			if (fl.exists()) {
				space = fl.getTotalSpace();
			}
		}

		return space;
	}

	protected Date folderCreatedDate(String folderName) {
		long date = 0;
		File fl = new File(folderName);
		if (fl.isDirectory())
			if (fl.exists())
				date = fl.lastModified();
		return new Date(date);
	}
}
