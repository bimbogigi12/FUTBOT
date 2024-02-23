package it.shadowlab.botFut.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	
	static Logger logger = LoggerFactory.getLogger(Config.class);

	Date lastLoadPlayer = null;
	String username = "";
	String password = "";
	boolean checkStat = false;
	int minOverall = 0;
	int monitorSize = 2; 
	String folderFUT = "";
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:MM"); 
	
	public Config(){
		loadConfig();
	}


	public void loadConfig() {
		logger.debug("Loading properties");
		File configFile = new File("config.properties");
		 
		try {
		    FileReader reader = new FileReader(configFile);
		    Properties props = new Properties();
		    props.load(reader);
		  
		    if (StringUtils.isNotEmpty(props.getProperty("lastLoadPlayer"))) {
		    	lastLoadPlayer = sdf.parse(props.getProperty("lastLoadPlayer"));
		    }
		    minOverall = Integer.valueOf(props.getProperty("minOverall"));
		    username = props.getProperty("userName");
		    password = props.getProperty("password");
		    checkStat = Boolean.getBoolean(props.getProperty("checkStat"));
		    monitorSize = Integer.valueOf(props.getProperty("monitorSize"));
		    folderFUT = props.getProperty("folderFUT");
		    
		    reader.close();
		} catch (FileNotFoundException ex) {
		    // file does not exist
		} catch (IOException ex) {
		    // I/O error
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("Loaded properties");
	}
	
	
	public void saveConfig() {
		logger.debug("Saving properties");
		File configFile = new File("config.properties");
		 
		try {
		    Properties props = new Properties();
		    
		    props.setProperty("lastLoadPlayer", sdf.format(lastLoadPlayer));
		    props.setProperty("minOverall", String.valueOf(minOverall));
		    props.setProperty("userName", username);
		    props.setProperty("password", password);
		    props.setProperty("checkStat", String.valueOf(checkStat));
		    props.setProperty("monitorSize", String.valueOf(monitorSize));
		    props.setProperty("folderFUT", folderFUT);
		    
		    FileWriter writer = new FileWriter(configFile);
		    props.store(writer, "host settings");
		    writer.close();
		} catch (FileNotFoundException ex) {
		    // file does not exist
		} catch (IOException ex) {
		    // I/O error
		}
		logger.debug("Saved properties");
	}


	public Date getLastLoadPlayer() {
		return lastLoadPlayer;
	}


	public void setLastLoadPlayer(Date lastLoadPlayer) {
		this.lastLoadPlayer = lastLoadPlayer;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public boolean isCheckStat() {
		return checkStat;
	}


	public void setCheckStat(boolean checkStat) {
		this.checkStat = checkStat;
	}


	public int getMinOverall() {
		return minOverall;
	}


	public void setMinOverall(int minOverall) {
		this.minOverall = minOverall;
	}


	public int getMonitorSize() {
		return monitorSize;
	}


	public void setMonitorSize(int monitorSize) {
		this.monitorSize = monitorSize;
	}


	public String getFolderFUT() {
		return folderFUT;
	}


	public void setFolderFUT(String folderFUT) {
		this.folderFUT = folderFUT;
	}
	
	
}
