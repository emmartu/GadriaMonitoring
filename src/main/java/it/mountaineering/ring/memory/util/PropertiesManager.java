package it.mountaineering.ring.memory.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import it.mountaineering.ring.memory.exception.CSVFormatPropertiesException;
import it.mountaineering.ring.memory.exception.NumberFormatPropertiesException;
import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.exception.WebcamPropertyIDException;
import it.mountaineering.ring.memory.webcam.WebcamProperty;

public class PropertiesManager {

	static Properties prop = new Properties();
	static InputStream input = null;

	static {
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String getStringPropertyByName(String propertyName) throws PropertiesException {
		String propertyStr = "";
		try {
			propertyStr = prop.getProperty(propertyName);
		} catch (Exception e) {
			throw new PropertiesException("Cannot Read Property "+propertyName);
		}
		
		if(propertyStr.equalsIgnoreCase("")) {
			throw new PropertiesException("Cannot Read Property "+propertyName);
		}
		
		return propertyStr;
	}
	
	
	private static long getNumberPropertyByName(String propertyName) throws PropertiesException {
		String propertyStr;
		
		try {
			propertyStr = prop.getProperty(propertyName);
		} catch (Exception e) {
			throw new PropertiesException("Cannot Read Property "+propertyName);
		}
		
		long propertyNumber;
		try {
			propertyNumber = Long.parseLong(propertyStr);
		} catch (NumberFormatException e) {
			throw new NumberFormatPropertiesException("Cannot Read Property "+propertyName);
		}
		
		return propertyNumber;
	}

	public static String getStorageFolder() {
		String storageFolder = "";
		
		try {
			storageFolder = getStringPropertyByName("StorageFolder");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return storageFolder;
	}

	public static Long getDiskSpace() {
		long diskSpace = 0L;
		
		try {
			diskSpace = getNumberPropertyByName("DiskSpace");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return diskSpace;
	}


	public static Long getMinDiskSpace() {
		long minDiskSpace = 0L;
		
		try {
			minDiskSpace = getNumberPropertyByName("MinDiskSpace");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return minDiskSpace;
	}

	public static String[] getWebcamArray() throws CSVFormatPropertiesException {
		String webcams = "";
		
		try {
			webcams = getStringPropertyByName("WebCams");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
				
		String[] webcamArray = null;
		
		try {
			webcamArray = webcams.split(",");
		}  catch (Exception e) {
			throw new CSVFormatPropertiesException("Cannot Read correct CSV on Property WebCams");
		}
		
		return webcamArray;
	}
	
	public static Long getVideoLength() throws PropertiesException {
		long videoLength = 0L;
		
		try {
			videoLength = getNumberPropertyByName("VideoLength");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return videoLength;
	}
	
	public static Long getOverlap() {
		long overlap = 0L;
		
		try {
			overlap = getNumberPropertyByName("Overlap");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return overlap;
	}

	public static WebcamProperty getWebcamPropertyById(String webcamId) throws WebcamPropertyIDException {
		WebcamProperty webcamProperty = new WebcamProperty();

		webcamProperty.setiD(webcamId);

		String enabledStr;
		try {
			enabledStr = prop.getProperty(webcamId+"_enabled");
		} catch (Exception e) {
			throw new WebcamPropertyIDException(webcamId+"_enabled");
		}
		
		boolean enabled = false;
		try {
			enabled = Boolean.parseBoolean(enabledStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		webcamProperty.setEnabled(enabled);
		
		String relativeStore = prop.getProperty(webcamId+"_relativeStore");
		webcamProperty.setRelativeStorageFolder(relativeStore);

		String webcamIP = prop.getProperty(webcamId+"_ip");
		webcamProperty.setIp(webcamIP);

		return webcamProperty;
	}

	public static String getAbsoluteStorageFolder() {
		String absoluteStorageFolder = prop.getProperty("AbsoluteStorageFolder");

		return absoluteStorageFolder;
	}

	
	public static void main(String[] args) {
		String bstr = "tr";
		
		boolean enabled = Boolean.parseBoolean(bstr);

		System.out.println("enabled: "+enabled);
	}
}