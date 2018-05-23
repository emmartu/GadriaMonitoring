package it.mountaineering.ring.memory.main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MainTest {

	Main mainToTest;
	
	@Before
	public void setUp() {
		Main mainTest = new Main();
		//mainToTest.
	}
	
	@Test
	public void testFreeDiskSpace() {
		mainToTest.freeDiskSpace();
		long diskSpace = mainToTest.getFolderSpace();
		//assertEquals(diskSpace<=);
		fail("Not yet implemented");
	}

	@Test
	public void testLaunchVlcRecord() {
		mainToTest.freeDiskSpace();
		fail("Not yet implemented");
	}

	@Test
	public void testIsDiskSpaceAvailable() {
		boolean hasSpace = mainToTest.isDiskSpaceAvailable();
		fail("Not yet implemented");
	}

	@Test
	public void testGetFolderSpace() {
		mainToTest.getFolderSpace();
		fail("Not yet implemented");
	}

	@Test
	public void testFolderCreatedDate() {
		fail("Not yet implemented");
	}

}
