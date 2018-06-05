package it.mountaineering.ring.memory.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import it.mountaineering.ring.memory.bean.DiskSpaceProperties;
import it.mountaineering.ring.memory.exception.PropertiesException;

public class DiskSpaceManagerTest {

	private static DiskSpaceManager diskSPaceManager;
	
	{
		diskSPaceManager = new DiskSpaceManager(PropertiesManager.getVideoAbsoluteStorageFolder(), PropertiesManager.getVideoMaxDiskSpace());
	}

	@Test
	public void testIsMemoryEnough() {
		UtilityForTests.deleteFileInDir("src//test//resources//TEST_FOLDER//");
		String configFile = "src/test/resources/configForMemory.properties";
		PropertiesManager.setConfigFile(configFile);
		PropertiesManager.checkIp = false;
		
		try {
			PropertiesManager.setupConfigProperties();
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		boolean isMemoryEnough = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough==true);
		
		File testFile_1 = new File("src//test//resources//TEST_FOLDER//test_file_1.txt");
		File testFile = new File("src//test//resources//test_file.txt");

		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_1);
		
		boolean isMemoryEnough_1 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_1==true);

		File testFile_2 = new File("src//test//resources//TEST_FOLDER//test_file_2.txt");
		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_2);

		boolean isMemoryEnough_3 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_3==true);

		File testFile_3 = new File("src//test//resources//TEST_FOLDER//test_file_3.txt");

		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_3);
		
		boolean isMemoryEnough_4 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_4==true);

		File testFile_4 = new File("src//test//resources//TEST_FOLDER//test_file_4.txt");
		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_4);
		
		boolean isMemoryEnough_5 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_5==true);

		File testFile_5 = new File("src//test//resources//TEST_FOLDER//test_file_5.txt");
		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_5);
		
		boolean isMemoryEnough_6 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_6==true);

		File testFile_6 = new File("src//test//resources//TEST_FOLDER//test_file_6.txt");
		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_6);
		
		boolean isMemoryEnough_7 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_7==true);

		File testFile_7 = new File("src//test//resources//TEST_FOLDER//test_file_7.txt");
		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_7);
		
		boolean isMemoryEnough_8 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_8==true);

		File testFile_8 = new File("src//test//resources//TEST_FOLDER//test_file_8.txt");
		UtilityForTests.copyFileUsingFileStreams(testFile, testFile_8);
		
		boolean isMemoryEnough_9 = diskSPaceManager.hasEnoughMemory();
		assertTrue(isMemoryEnough_9==false);
	}

	@Test
	public void testCalculateSafetyThreshold() {
		DiskSpaceProperties diskSpaceProperties = new DiskSpaceProperties();
		diskSpaceProperties.setFileNumber(100L);
		diskSpaceProperties.setFolderSize(15574L);

		Long calculatedThreshOld = diskSPaceManager.calculateSafetyThreshold(diskSpaceProperties);
		assertTrue(233L==calculatedThreshOld);
	}
	
	@Test
	public void testDeleteOldestFilesFromMemory() {
		UtilityForTests.deleteFileInDir("src//test//resources//TEST_FOLDER//");
		String filePath = "src//test//resources//TEST_FOLDER//";
		File testFile = new File("src//test//resources//test_file.txt");

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
		String fileName = "w1_"+timeStamp+".txt";
		File file = new File(filePath+fileName);
		UtilityForTests.copyFileUsingFileStreams(testFile, file);
		//DiskSpaceManager.addLatestFile(file);
		
		String timeStamp2 = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
		String fileName2 = "w1_"+timeStamp2+".txt";
		File file2 = new File(filePath+fileName2);
		UtilityForTests.copyFileUsingFileStreams(testFile, file2);
		//DiskSpaceManager.addLatestFile(file2);

		String timeStamp3 = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
		String fileName3 = "w1_"+timeStamp3+".txt";
		File file3 = new File(filePath+fileName3);
		UtilityForTests.copyFileUsingFileStreams(testFile, file3);
		//DiskSpaceManager.addLatestFile(file3);

		String timeStamp4 = new SimpleDateFormat("yyyy-MM-dd@HH-mm-ss.S").format(new Date());
		String fileName4 = "w1_"+timeStamp4+".txt";
		File file4 = new File(filePath+fileName4);
		UtilityForTests.copyFileUsingFileStreams(testFile, file4);
		//DiskSpaceManager.addLatestFile(file4);
		
		diskSPaceManager.deleteOldestFilesFromMemory();

		boolean findFile = UtilityForTests.findFile(fileName, new File(filePath));
		
		assertTrue(findFile==false);
		
		UtilityForTests.renameFile(filePath+fileName2,filePath+"EVENT_"+fileName2);

		boolean findFile2 = UtilityForTests.findFile(fileName2, new File(filePath));
		
		assertTrue(findFile2==false);

		diskSPaceManager.deleteOldestFilesFromMemory();
		
		boolean findFile3 = UtilityForTests.findFile(fileName3, new File(filePath));
		
		assertTrue(findFile3==false);		
	} 	
}