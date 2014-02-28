package com.sample;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import net.lingala.zip4j.exception.ZipException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.tfs.core.clients.versioncontrol.VersionControlClient;
import com.microsoft.tfs.core.clients.versioncontrol.soapextensions.BranchObject;
import com.microsoft.tfs.core.clients.versioncontrol.soapextensions.ItemIdentifier;
import com.microsoft.tfs.core.clients.versioncontrol.soapextensions.RecursionType;
import com.microsoft.tfs.core.clients.versioncontrol.soapextensions.Workspace;
import com.sample.utils.TFSTeamProject;
import com.sample.utils.Utils;

/**
 * 
 * @author gsingh
 *
 */
public class TF {
	
	private static final Logger logger = LoggerFactory.getLogger(TF.class);
	
	private static String branchName = ""; 
	private static final String PATH_SEPARATOR = "/"; //TFS server uses Unix path separator
	private static TFSTeamProject tfsTeamProject ;
	private static final String SDK_NATIVE_LIB_URL = "http://quantrepository/artifactory/list/ext-release-local/"
			+ "com/microsoft/tfs/native/com.microsoft.tfs.native/11.0.0/com.microsoft.tfs.native-11.0.0.jar";
	private static final String TFS_URL_DEFAULT = "http://tfs:8080/tfs/DefaultCollection";
	
	public TF(String tfsUrl, File projectLocalPath) {
		try {
			//set up native libs
			setup();
			//make connection
			tfsTeamProject = TFSTeamProject.getInstance(tfsUrl);
			//cache branch name for current directory 
			if(projectLocalPath == null) projectLocalPath = new File(".");
			setBranchName(projectLocalPath.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TF() {
		this(TFS_URL_DEFAULT, null);
	}
	
	public String getVersionName()  {
		if(branchName.isEmpty() ) throw new RuntimeException("couldn't determine branch name");
		if(Character.isDigit(branchName.charAt(0))) {
			return  branchName;
		} else {
			return branchName + "-SNAPSHOT";
		}
	}

	/**
	 * @throws UnknownHostException
	 */
	public void setBranchName(String localPath)  {
		if(localPath == null) localPath = ".";
		logger.info("Local path : " + localPath);
		for (Workspace ws : tfsTeamProject.getWorkspaces()) {
			String serverPath = ws.getMappedServerPath(new File(localPath).getAbsolutePath());
			logger.info("Server path mapped : " + serverPath);
			if (serverPath != null) {
				String branchPath = getBranchPath(serverPath);
				logger.info("Branch path for server path " + branchPath);
				if(branchPath != null ) {
					int i = branchPath.lastIndexOf(PATH_SEPARATOR);
					branchName = branchPath.substring(i + 1).toUpperCase();
					logger.info("Branch Name : "+ branchName);
					break;
				}
			}
		}
	}

	/**
	 * @param tfsClient
	 * @param path
	 */
	private  String getBranchPath(String path) {
		logger.info("Getting branch path for :: " + path);
		VersionControlClient tfsClient = tfsTeamProject.getTfsClient();
		
		// get branch object
		BranchObject[] bObjects = tfsClient.queryBranchObjects(new ItemIdentifier(path), RecursionType.NONE);

		if (bObjects != null && bObjects.length == 0 ) { // walk up the path
			logger.info( path + " is not a branch..");
			int i = path.lastIndexOf(PATH_SEPARATOR);
			path = path.substring(0, i);
			logger.info("Will not check parent path : " + path);
			if (path.equals("$")) return null; // yeild at root path
			return getBranchPath(path);
		}
		return bObjects[0].getProperties().getRootItem().getItem();
	}
		

	/**
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ZipException
	 */
	private  void setup() throws IOException, MalformedURLException,
			ZipException {
		logger.info("setting up native library...");
		File tempFolder = Utils.createTempFolder("temp", "");
		File nativeJar = Utils.downloadFile(new URL(SDK_NATIVE_LIB_URL),
				"native.jar", tempFolder);
		Utils.unzip(nativeJar, tempFolder);
		System.setProperty("com.microsoft.tfs.jni.native.base-directory",
				new File(tempFolder, "native").getAbsolutePath());
		logger.info("TFS native library set...");
	}
}
