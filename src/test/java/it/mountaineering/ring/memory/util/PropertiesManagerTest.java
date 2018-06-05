package it.mountaineering.ring.memory.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import it.mountaineering.ring.memory.bean.WebcamProperty;
import it.mountaineering.ring.memory.exception.CSVFormatPropertiesException;
import it.mountaineering.ring.memory.exception.NumberFormatPropertiesException;
import it.mountaineering.ring.memory.exception.PropertiesException;

public class PropertiesManagerTest {

	Map<String,String> propertiesMap;
	Map<String,WebcamProperty> webcamPropertiesMap;
	String[] webcamArray;
	
	public void setUp() {
		propertiesMap = new HashMap<String, String>();
		propertiesMap.put("AbsoluteStorageFolder", "C:\\Users\\Lele\\Documents\\LavoroWebCamMobotix\\TEST_FOLDER\\");
		propertiesMap.put("DiskSpace", "10000");
		propertiesMap.put("VideoLength", "30");
		propertiesMap.put("Overlap", "2");

		webcamArray = new String[2];
		webcamArray[0] = "w1";
		webcamArray[1] = "w2"; 
		
		webcamPropertiesMap = new HashMap<String, WebcamProperty>();
		WebcamProperty w1WebcamProperty = new WebcamProperty();
		w1WebcamProperty.setEnabled(false);
		w1WebcamProperty.setiD("w1");
		w1WebcamProperty.setIp("111.0.0.1");
		w1WebcamProperty.setVideoRelativeStorageFolder("W1");
		webcamPropertiesMap.put("w1", w1WebcamProperty);
		
		WebcamProperty w2WebcamProperty = new WebcamProperty();
		w2WebcamProperty.setEnabled(false);
		w2WebcamProperty.setiD("w2");
		w2WebcamProperty.setIp("111.0.0.2");
		w2WebcamProperty.setVideoRelativeStorageFolder("W2");
		webcamPropertiesMap.put("w2", w2WebcamProperty);

	}

	public void testSetupConfigProperties() {
		
		fail("Not yet implemented");
	}

	@Test(expected = PropertiesException.class)
	public void testGetAbsoluteStorageFolderFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailAbsFolder_A.properties";
		PropertiesManager.setConfigFile(configFile);
		
		String storageFolder = PropertiesManager.getVideoAbsoluteStorageFolderFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetAbsoluteStorageFolderFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailAbsFolder_B.properties";
		PropertiesManager.setConfigFile(configFile);
		
		String storageFolder = PropertiesManager.getVideoAbsoluteStorageFolderFromConfigProperties();
	}

	@Test(expected = NumberFormatPropertiesException.class)
	public void testGetDiskSpaceFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailA.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long diskSpace = PropertiesManager.getVideoMaxDiskSpaceFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetDiskSpaceFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailB.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long diskSpace = PropertiesManager.getVideoMaxDiskSpaceFromConfigProperties();
	}

	@Test(expected = NumberFormatPropertiesException.class)
	public void testGetVideoLengthFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailA.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long videoLength = PropertiesManager.getVideoLengthFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetVideoLengthFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailB.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long videoLength = PropertiesManager.getVideoLengthFromConfigProperties();
	}

	@Test(expected = NumberFormatPropertiesException.class)
	public void testGetOverlapFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailA.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long overlap = PropertiesManager.getOverlapFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetOverlapFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailB.properties";
		PropertiesManager.setConfigFile(configFile);
		
		Long overlap = PropertiesManager.getOverlapFromConfigProperties();
	}


	@Test(expected = CSVFormatPropertiesException.class)
	public void testGetWebcamNamesFromConfigProperties_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailA.properties";
		PropertiesManager.setConfigFile(configFile);
		
		String[] webcamNames = PropertiesManager.getWebcamNamesFromConfigProperties();
	}

	@Test(expected = PropertiesException.class)
	public void testGetWebcamNamesFromConfigProperties_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailB.properties";
		PropertiesManager.setConfigFile(configFile);
		
		String[] webcamNames = PropertiesManager.getWebcamNamesFromConfigProperties();
	}
	
	/*
	@Test(expected = WebcamPropertyIDException.class)
	public void testGetWebcamPropertyFromConfigPropertiesById_A() throws PropertiesException {
		String configFile = "src/test/resources/configFailA.properties";
		PropertiesManager.setConfigFile(configFile);
		
		WebcamProperty webcamNames = PropertiesManager.getWebcamPropertyFromConfigPropertiesById("w2");
	}*/

	//@Test(expected = UnreachableIpException.class)
	public void testGetWebcamPropertyFromConfigPropertiesById_B() throws PropertiesException {
		String configFile = "src/test/resources/configFailB.properties";
		PropertiesManager.setConfigFile(configFile);
		
		WebcamProperty webcamNames = PropertiesManager.getWebcamPropertyFromConfigPropertiesById("w2");
	}

	
	@Test
	public void testSetupConfig() throws PropertiesException {
		setUp();
		String configFile = "src/test/resources/config.properties";
		PropertiesManager.setConfigFile(configFile);
		PropertiesManager.setupConfigProperties();
		
		assertTrue(this.propertiesMap.toString().equals(PropertiesManager.propertiesMap.toString()));
		assertTrue(Arrays.equals(this.webcamArray, PropertiesManager.webcamArray));
		assertTrue(this.webcamPropertiesMap.get("w1").equals(PropertiesManager.webcamPropertiesMap.get("w1")));
		assertTrue(this.webcamPropertiesMap.get("w2").equals(PropertiesManager.webcamPropertiesMap.get("w2")));
	}

}
