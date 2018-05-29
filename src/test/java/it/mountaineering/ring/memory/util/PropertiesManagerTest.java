package it.mountaineering.ring.memory.util;

import static org.junit.Assert.*;

import org.junit.Rule;

import it.mountaineering.ring.memory.exception.NumberFormatPropertiesException;
import it.mountaineering.ring.memory.exception.PropertiesException;

import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PropertiesManagerTest {

	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	public void setUp() {
	}

	public void testSetupConfigProperties() {
		
		fail("Not yet implemented");
	}

	@Test(expected = PropertiesException.class)
	public void testGetAbsoluteStorageFolderFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailAbsFolder_A.properties";
		PropertiesManager.setConfigFile(configFile);
		
		String storageFolder = PropertiesManager.getAbsoluteStorageFolderFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetAbsoluteStorageFolderFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailAbsFolder_B.properties";
		PropertiesManager.setConfigFile(configFile);
		
		String storageFolder = PropertiesManager.getAbsoluteStorageFolderFromConfigProperties();
	}

	@Test(expected = NumberFormatPropertiesException.class)
	public void testGetDiskSpaceFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailDiskSpace_A.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long diskSpace = PropertiesManager.getDiskSpaceFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetDiskSpaceFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailDiskSpace_B.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long diskSpace = PropertiesManager.getDiskSpaceFromConfigProperties();
	}

}
