package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UtilityForTests {

	public static void copyFileUsingFileStreams(File source, File dest) {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static void deleteFileInDir(String path) {
		File directory = new File(path);

		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				file.delete();
			}
		}
	}

	public static boolean findFile(String name, File file) {
		File[] list = file.listFiles();
		if (list != null)
			for (File fil : list) {
				if (fil.isDirectory()) {
					return findFile(name, fil);
				} else if (name.equalsIgnoreCase(fil.getName())) {
					return true;
				}
			}

		return false;
	}

	public static boolean renameFile(String oldFile, String newFile) {

		File oldfile = new File(oldFile);
		File newfile = new File(newFile);

		if (oldfile.renameTo(newfile)) {
			return true;
		}

		return false;
	}

	public static boolean createNewFile(String fileName) {
		try {

			File file = new File(fileName);

			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
