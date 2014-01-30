package com.sapient.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * java in eclipse start in project folder 
 * For example if C:\Users\GSingh\Source\EclipseWS\PropertyReader is the project folder
 * eclipse would do something like 
 * %> cd C:\Users\GSingh\Source\EclipseWS\PropertyReader
 * C:\Users\GSingh\Source\EclipseWS\PropertyReader %> java -cp ./bin com.sapient.main.Main 
 * 
 * Class-Loader will take property from C:\Users\GSingh\Source\EclipseWS\PropertyReader\bin\com\sapient\main
 * 
 * FileInputStream will start looking for files in folder java 
 * started that is C:\Users\GSingh\Source\EclipseWS\PropertyReader
 * 
 */

public class Main {

	private static String PROPERTY_FILE = "property.file";
	private static Properties embededProp = new Properties();
	private static Properties localProp = new Properties();

	public static void main(String[] args) {
		if (System.getProperty(PROPERTY_FILE) == null) {
			// read embeded property file
			InputStream is = null;
			try {
				try {
					is = Main.class.getResourceAsStream("embeded.properties");
					embededProp.load(is);
				} finally {
					if (is != null)
						is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// read from file location property file
			try {
				try {
					localProp.load(new FileInputStream("local.properties"));
				} finally {
					if (is != null)
						is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(embededProp);
		System.out.println(localProp);
	}
}
