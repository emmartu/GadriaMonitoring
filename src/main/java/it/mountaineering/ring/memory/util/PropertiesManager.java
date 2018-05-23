package it.mountaineering.ring.memory.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

	public static String getStorageFolder() {
		String storageFolder = prop.getProperty("StorageFolder");

		return storageFolder;
	}

}