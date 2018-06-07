package it.mountaineering.ring.memory.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import it.mountaineering.ring.memory.bean.DiskSpaceProperties;
import it.mountaineering.ring.memory.bean.FileWithCreationTime;

public class DiskSpaceManager {

	private static final java.util.logging.Logger log = Logger.getLogger(DiskSpaceManager.class.getName());
	private DiskSpaceProperties diskSpaceProperties;
	String storageFolder;
	Long maxDiskSpace;
	public Long size = 0L;
	
	public DiskSpaceManager(String storageFolder, Long maxDiskSpace) {
		this.storageFolder = storageFolder;
		this.maxDiskSpace = maxDiskSpace;
	}

	public boolean hasEnoughMemory() {
		//log.info("hasEnoughMemory()");
		System.out.println("check has enough memory on folder: "+storageFolder);
		File storageFile = new File(storageFolder);

		if(diskSpaceProperties==null||
		   diskSpaceProperties.getFileNumber()==0L||
		   diskSpaceProperties.getFolderSize()==0L) {
			diskSpaceProperties = getDiskSpaceProperties(storageFile);
		}

		Long safetythreshold = calculateSafetyThreshold(diskSpaceProperties);
		Long freeSpace = maxDiskSpace - diskSpaceProperties.getFolderSize();
		System.out.println("freeSpace: (maxDiskSpace)"+maxDiskSpace+" - (actual folder busy size)"+diskSpaceProperties.getFolderSize()+" = (freespace)"+freeSpace);

		//log.info("freeSpace: "+maxDiskSpace+" - "+diskSpaceProperties.getFolderSize()+" = "+freeSpace);

		if (freeSpace >= safetythreshold) {
			//log.info("OK --> freeSpace >= safetythreshold");
			System.out.println("OK --> freeSpace >= safetythreshold");
			return true;
		}

		//log.info("NO OK --> freeSpace < safetythreshold");
		System.out.println("NO OK --> freeSpace < safetythreshold");
		return false;
	}

	public void deleteOldestFilesFromMemory() {
		//log.info("deleteOldestFilesFromMemory");
		System.out.println("delete Oldest Files From Memory");

		Collection<Long> unsortedEpochList = diskSpaceProperties.getFileMap().keySet();
		List<Long> sorted = asSortedList(unsortedEpochList);
		Long firstItem = sorted.get(0);
		//log.info("sortedList first item: "+firstItem);
		System.out.println("sorted time file map, first key: "+firstItem);
		File file = diskSpaceProperties.getFileMap().get(firstItem);
		System.out.println("sorted time file map, first file to remove: "+file.getAbsolutePath()+", size: "+file.length());
		//log.info("first item, file to remove: "+file.getName()+" path: "+file.getAbsolutePath());

		if (file.isFile()) {
			Long size = file.length();
			diskSpaceProperties.removeFolderSize(size);
			diskSpaceProperties.removeFileNumber(1L);
			diskSpaceProperties.getFileMap().remove(firstItem);
			boolean deleted = file.delete();
			//log.info("file deleted: "+deleted);
			System.out.println("file deleted: "+deleted);
		}else {
			System.out.println("invalid file to remove!"+file.getName());
		}
	}

	private <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}

	protected Long calculateSafetyThreshold(DiskSpaceProperties diskSpaceProperties) {
		//log.info("init calculateSafetyThreshold");
		System.out.println("calculateSafetyThreshold");

		Double safetythreshold = new Double(0);
		Double folderSize = new Double(diskSpaceProperties.getFolderSize());
		
		safetythreshold = (folderSize / diskSpaceProperties.getFileNumber());
		Double fiftyPercent = (safetythreshold / 100) * 50;
		safetythreshold = safetythreshold + fiftyPercent;

		Long longSafetythreshold = (new Double(safetythreshold)).longValue();
		System.out.println("calculateSafetyThreshold: "+longSafetythreshold);

		return longSafetythreshold;
	}

	
	
	protected DiskSpaceProperties getDiskSpaceProperties(File directory) {
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

	public void addLatestFile(FileWithCreationTime fileWithCreationTime) {
		//log.info("add Latest File name: "+fileWithCreationTime.getFile().getName()+", size: "+fileWithCreationTime.getFile().length()+", creation time: "+fileWithCreationTime.getCreationTime());
		System.out.println("add Latest File name: "+fileWithCreationTime.getFile().getName()+", size: "+fileWithCreationTime.getFile().length()+", creation time: "+fileWithCreationTime.getCreationTime());
		if(diskSpaceProperties==null) {
			diskSpaceProperties = new DiskSpaceProperties();
		}

		diskSpaceProperties.addFileNumber(1L);
		Long size = fileWithCreationTime.getFile().length();
		diskSpaceProperties.addFolderSize(size);
		diskSpaceProperties.putFileInMap(fileWithCreationTime);

		//log.info("addLatestFile --> set diskSpaceProperties, file number: "+diskSpaceProperties.getFileNumber()+", folder size: "+diskSpaceProperties.getFolderSize()+", file map: "+diskSpaceProperties.getFileMap().toString());
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
