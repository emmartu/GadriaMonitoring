package it.mountaineering.ring.memory.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.Before;
import org.junit.Test;

import it.mountaineering.ring.memory.bean.DiskSpaceProperties;
import it.mountaineering.ring.memory.bean.FileWithCreationTime;
import it.mountaineering.ring.memory.util.DiskSpaceManager;

public class MainTest {

	@Test
	public void setUp() {

		File folder = new File("C:\\Users\\Lele\\Documents\\LavoroWebCamMobotix\\AVI_Exports\\");
		DiskSpaceProperties dsp = getDiskSpaceProperties(folder);
	}
	
	
	public DiskSpaceProperties getDiskSpaceProperties(File directory) {
		//log.info("init DiskSpaceProperties");
		System.out.println("********  init DiskSpaceProperties  **********");
		DiskSpaceProperties diskSpaceFile = new DiskSpaceProperties();

		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				System.out.println("-- getDiskSpaceProperties -- add file: "+file+" on DiskSpaceProperties");

				diskSpaceFile.addFolderSize(file.length());
				diskSpaceFile.addFileNumber(1L);
				FileWithCreationTime fileWithCreationTime = new FileWithCreationTime(file.getAbsolutePath(), getFileCreationEpoch(file));
				diskSpaceFile.putFileInMap(fileWithCreationTime);
			} else {
				System.out.println("-- getDiskSpaceProperties -- get subfolder properties: "+file);

				DiskSpaceProperties diskSpaceFileTemp = getDiskSpaceProperties(file);
				diskSpaceFile.addFolderSize(diskSpaceFileTemp.getFolderSize());
				diskSpaceFile.addFileNumber(diskSpaceFileTemp.getFileNumber());
				diskSpaceFile.mergeFileMap(diskSpaceFileTemp.getFileMap());
			}
		}

		//log.info("return diskSpaceFile FolderSize: "+diskSpaceFile.getFolderSize()+", FileNumber: "+diskSpaceFile.getFileNumber()+", fileMap: "+diskSpaceFile.getFileMap().toString());
		System.out.println("return diskSpaceFile for folder: "+directory+" FolderSize: "+diskSpaceFile.getFolderSize()+", FileNumber: "+diskSpaceFile.getFileNumber());
		System.out.println("********  END DiskSpaceProperties  **********");

		return diskSpaceFile;
	}

	
	private long getFileCreationEpoch(File file) {
		try {
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			return attr.creationTime().toInstant().toEpochMilli();
		} catch (IOException e) {
			throw new RuntimeException(file.getAbsolutePath(), e);
		}
	}

}
