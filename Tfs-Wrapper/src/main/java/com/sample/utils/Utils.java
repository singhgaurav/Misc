package com.sample.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * 
 * @author gsingh
 *
 */
public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);

	/**
	 * Downloads file at location URL to a given folder with given name
	 * 
	 * @param url
	 *            Source location of file
	 * @param fileName
	 *            Name of the target file
	 * @param toFolder
	 *            Target folder
	 * @return
	 * @throws IOException
	 */
	public static File downloadFile(URL url, String fileName, File toFolder)
			throws IOException {
		logger.info("Downloading file " + url.getPath() + "to Folder "
				+ toFolder + "..........");
		
		File targetFile = new File(toFolder, fileName);
		FileUtils.copyURLToFile(url, targetFile);

		logger.info("Downloaded file " + url.getPath() + "to Folder "
				+ toFolder);
		return targetFile;

	}

	/**
	 * Create temporary folder with given prefix and name.
	 * 
	 * @param prefix
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static File createTempFolder(String prefix, String name)
			throws IOException {
		final File temp;
		temp = File.createTempFile(prefix, name);
		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		}
		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "+ temp.getAbsolutePath());
		}
		logger.info("Created temporary folder" + temp);
		return (temp);
	}

	public static void unzip(File source, File destination) throws ZipException {

		ZipFile zipFile = new ZipFile(source);
		Preconditions.checkArgument(zipFile.isValidZipFile(),
				"Provide valid zip file...");
		zipFile.extractAll(destination.getAbsolutePath());
		logger.info("Extracted " + source + "at " + destination);

	}

}
