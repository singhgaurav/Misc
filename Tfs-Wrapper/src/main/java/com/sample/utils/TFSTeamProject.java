package com.sample.utils;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.tfs.core.TFSTeamProjectCollection;
import com.microsoft.tfs.core.clients.versioncontrol.VersionControlClient;
import com.microsoft.tfs.core.clients.versioncontrol.soapextensions.Workspace;
import com.microsoft.tfs.core.httpclient.Credentials;
import com.microsoft.tfs.core.httpclient.DefaultNTCredentials;

/**
 * Create client and caches user workspaces
 * 
 * @author gsingh
 * 
 */
public class TFSTeamProject {

	private static final Logger logger = LoggerFactory.getLogger(TFSTeamProject.class);
	
	private VersionControlClient tfsClient;
	private Workspace[] workspaces;
	private static TFSTeamProject teamProject = null;

	private TFSTeamProject() {
	}

	public static synchronized TFSTeamProject getInstance(final String serverURL)
			throws Exception {
		if (teamProject == null) {
			teamProject = new TFSTeamProject();
			VersionControlClient client = teamProject.createClient(serverURL);
			teamProject.setClientWorkspaces(client);
		}
		return teamProject;
	}

	private void setClientWorkspaces(VersionControlClient tfsClient) throws UnknownHostException {
		String userName = System.getProperty("user.name");
		logger.info("Creating TFS Java Client for user : " + userName);
		String hostName = InetAddress.getLocalHost().getHostName();
		try { // try getting the local workspace. 
			Workspace workspace = tfsClient.getWorkspace(".");
			workspaces 	= new Workspace [] {workspace};
		}catch (Exception ex) { // 
			logger.info("Exception is getting local workspace querying server workspace ");
			workspaces 	= tfsClient.queryWorkspaces(null, userName, hostName);
		}

		logger.info("Number of workspaces found for this user local workspace  " + workspaces.length);
	}

	private VersionControlClient createClient(final String serverURL) throws URISyntaxException {
		URI serverURI = new URI(serverURL);
		Credentials credentials = new DefaultNTCredentials();
		TFSTeamProjectCollection pCollection = new TFSTeamProjectCollection(
				serverURI, credentials);
		tfsClient = pCollection.getVersionControlClient();
		return tfsClient;
	}

	public VersionControlClient getTfsClient() {
		return tfsClient;
	}

	public Workspace[] getWorkspaces() {
		return workspaces;
	}

}
