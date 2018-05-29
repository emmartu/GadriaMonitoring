package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.exception.BooleanStringPropertyException;
import it.mountaineering.ring.memory.exception.CSVFormatPropertiesException;
import it.mountaineering.ring.memory.exception.NumberFormatPropertiesException;
import it.mountaineering.ring.memory.exception.PropertiesException;
import it.mountaineering.ring.memory.exception.UnreachableIpException;
import it.mountaineering.ring.memory.exception.WebcamPropertyIDException;
import it.mountaineering.ring.memory.webcam.WebcamProperty;

public class PropertiesManager {

	private static final java.util.logging.Logger log = Logger.getLogger(PropertiesManager.class.getName());

	private static Map<String,String> propertiesMap = new HashMap<String, String>();
	private static Map<String,WebcamProperty> webcamPropertiesMap = new HashMap<String, WebcamProperty>();
	private static String[] webcamArray;

	private static Properties prop = new Properties();
	private static InputStream input = null;
	
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

	protected static void setConfigFile(String configFile) {
		try {
			input = new FileInputStream(configFile);
			prop.clear();
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
	
	public static void setupConfigProperties() throws PropertiesException {
		log.info("***********************************************");
		log.info("**** Get Properties From config.properties ****");
		log.info("***********************************************");

		String absoluteStorageFolder = getAbsoluteStorageFolderFromConfigProperties();
		log.info("AbsoluteStorageFolder: "+absoluteStorageFolder);
		propertiesMap.put("AbsoluteStorageFolder", absoluteStorageFolder);
		
		Long diskSpace = getDiskSpaceFromConfigProperties();
		log.info("DiskSpace: "+diskSpace);
		propertiesMap.put("DiskSpace", String.valueOf(diskSpace));
		
		Long videoLength = getVideoLengthFromConfigProperties();
		log.info("VideoLength: "+videoLength);
		propertiesMap.put("VideoLength", String.valueOf(videoLength));

		Long overlap = getOverlapFromConfigProperties();
		log.info("Overlap: "+overlap);
		propertiesMap.put("Overlap", String.valueOf(overlap));
		
		try {
			webcamArray = getWebcamNamesFromConfigProperties();
		} catch (CSVFormatPropertiesException e1) {
			log.info("error occured reading webcam names property");
			e1.printStackTrace();
		}
		
		log.info("webcamArray: "+Arrays.toString(webcamArray));
		
		for (int i = 0; i < webcamArray.length; i++) {
			WebcamProperty webcamProperty = null;
			try {
				webcamProperty = getWebcamPropertyFromConfigPropertiesById(webcamArray[i]);
			} catch (WebcamPropertyIDException e) {
				log.info("error occured reading webcam "+webcamArray[i]+" property ");
				e.printStackTrace();
				continue;
			}
			
			webcamPropertiesMap.put(webcamArray[i], webcamProperty);			
		}
		
		log.info("***********************************************");
		log.info("********* Properties setup Complete ***********");
		log.info("***********************************************");
	}
	

	private static String getStringPropertyByName(String propertyName) throws PropertiesException {
		String propertyStr = "";
		propertyStr = prop.getProperty(propertyName);
		
		if(propertyStr == null || propertyStr.equalsIgnoreCase("")) {
			throw new PropertiesException("Cannot Read Property "+propertyName);
		}
		
		return propertyStr;
	}
	
	
	private static long getNumberPropertyByName(String propertyName) throws PropertiesException, NumberFormatPropertiesException {
		String propertyStr;
		
		propertyStr = prop.getProperty(propertyName);

		if(propertyStr == null || propertyStr.equalsIgnoreCase("")) {
			throw new PropertiesException("Cannot Read Property "+propertyName+", property is null");
		}
		
		long propertyNumber;
		try {
			propertyNumber = Long.parseLong(propertyStr);
		} catch (NumberFormatException e) {
			throw new NumberFormatPropertiesException("Cannot Read Property "+propertyName+", malformed number");
		}
		
		return propertyNumber;
	}

	protected static String getAbsoluteStorageFolderFromConfigProperties() throws PropertiesException {
		String storageFolder = "";

		storageFolder = getStringPropertyByName("AbsoluteStorageFolder");

		File storageDirectory = new File(storageFolder);

		if (!storageDirectory.exists()||!storageDirectory.isDirectory()) {
			throw new PropertiesException("AbsoluteStorageFolder property cannot access Directory "+storageFolder);
		}

		return storageFolder;
	}
	
	public static String getAbsoluteStorageFolder() {
		return propertiesMap.get("AbsoluteStorageFolder");
	}
	
	protected static Long getDiskSpaceFromConfigProperties() throws PropertiesException, NumberFormatPropertiesException {
		long diskSpace = 0L;
		
		diskSpace = getNumberPropertyByName("DiskSpace");
		
		return diskSpace;
	}

	public static Long getDiskSpace() {
		String diskSpaceStr = propertiesMap.get("DiskSpace");
		
		Long diskSpace = Long.parseLong(diskSpaceStr);

		return diskSpace;
	}

	private static String[] getWebcamNamesFromConfigProperties() throws CSVFormatPropertiesException {
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
	
	public static String[] getWebcamNames() {
		return webcamArray;
	}

	private static Long getVideoLengthFromConfigProperties() {
		long videoLength = 0L;
		
		try {
			videoLength = getNumberPropertyByName("VideoLength");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return videoLength;
	}


	public static Long getVideoLength(){
		String videoLengthStr = propertiesMap.get("VideoLength");
		
		Long videoLength = Long.parseLong(videoLengthStr);

		return videoLength;
	}
	
	private static Long getOverlapFromConfigProperties() {
		long overlap = 0L;
		
		try {
			overlap = getNumberPropertyByName("Overlap");
		} catch (PropertiesException e) {
			e.printStackTrace();
		}
		
		return overlap;
	}

	public static Long getOverlap() {
		String overlapStr = propertiesMap.get("OverlapStr");
		
		Long overlap = Long.parseLong(overlapStr);

		return overlap;
	}

	public static WebcamProperty getWebcamPropertyById(String webcamId) {
		WebcamProperty webcamProperty = webcamPropertiesMap.get(webcamId);
		
		return webcamProperty;
	}
	
	private static WebcamProperty getWebcamPropertyFromConfigPropertiesById(String webcamId) throws WebcamPropertyIDException {
		WebcamProperty webcamProperty = new WebcamProperty();

		webcamProperty.setiD(webcamId);

		String enabledStr;
		try {
			enabledStr = prop.getProperty(webcamId+"_enabled");
		} catch (Exception e) {
			throw new WebcamPropertyIDException("Cannot read Property "+webcamId+"_enabled");
		}
		
		boolean enabled = false;
		
		if(!isValidBooleanString(enabledStr)) {
			throw new BooleanStringPropertyException("");
		}

		enabled = Boolean.parseBoolean(enabledStr);
		webcamProperty.setEnabled(enabled);
		
		String relativeStore = "";
		try {
			relativeStore = prop.getProperty(webcamId+"_relativeStore");
		} catch (Exception e) {
			throw new WebcamPropertyIDException("Cannot read Property "+webcamId+"_relativeStore");
		}
		
		webcamProperty.setRelativeStorageFolder(relativeStore);

		String webcamIP = "";
		try {
			webcamIP = prop.getProperty(webcamId+"_ip");
		} catch (Exception e) {
			throw new WebcamPropertyIDException("Cannot read Property "+webcamId+"_ip");
		}
		
		if(!isIPOnline(webcamIP)){
			throw new UnreachableIpException("Unable to reach "+webcamIP+" for webcam "+webcamId);
		}
		
		webcamProperty.setIp(webcamIP);

		return webcamProperty;
	}

	
	private static boolean isIPOnline(String webcamIP) {
		
		if(PingIp.isPingReachable(webcamIP)){
			return true;
		}
		
		return false;
	}


	private static boolean isValidBooleanString(String enabledStr) {
		if (enabledStr.equalsIgnoreCase("true") || enabledStr.equalsIgnoreCase("false")) {
			return true;
		}
		
		return false;
	}


	public static void main(String[] args) {
		String bstr = "tr";
		
		boolean enabled = Boolean.parseBoolean(bstr);

		System.out.println("enabled: "+enabled);
	}
}