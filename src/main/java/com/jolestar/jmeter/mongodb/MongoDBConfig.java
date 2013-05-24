package com.jolestar.jmeter.mongodb;

import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.util.NoConfigMerge;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoDBConfig extends ConfigTestElement implements ConfigElement, TestBean,
		TestStateListener, NoConfigMerge {

	private static final Logger log = LoggingManager.getLoggerForClass();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient String varName;
	private transient String host;
	private transient int port;
	private transient String dbName;
	private transient String username;
	private transient String password;
	private transient DB dbInstance;
	

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}
	

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String db) {
		this.dbName = db;
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
	
	public String getIdentityString(){
		return this.host+":"+this.port+"/"+this.dbName;
	}


	@Override
	public void testEnded() {
		log.info("close mongo dbName :" + this.varName);
		JMeterVariables variables = JMeterContextService.getContext()
				.getVariables();
		variables.remove(this.varName);
		this.dbInstance.getMongo().close();
	}

	@Override
	public void testEnded(String host) {
		this.testEnded();
	}

	@Override
	public void testStarted() {
		JMeterVariables variables = JMeterContextService.getContext()
				.getVariables();
		if (variables.getObject(this.varName) != null) {
			log.error("Test error: Multiple MongoDB config called " + varName);
			return;
		}
		log.info("create mongo dbName config :" + this.toString());
		
		try {
			this.dbInstance = Mongo.connect(new DBAddress(this.getHost(), this.getPort(), this.getDbName()));
			if(!StringUtils.isBlank(this.getUsername()) && !StringUtils.isBlank(this.getPassword())){
				boolean result = this.dbInstance.authenticate(this.getUsername(), this.getPassword().toCharArray());
				if(!result){
					log.error("authenticate to "+this.getIdentityString()+" fail.");
				}
			}
			variables.putObject(varName, this);
		} catch (UnknownHostException e) {
			log.error("connect to "+this.getIdentityString()+" fail. msg:"+e.getMessage());
		}
	}
	
	public DBCollection getCollection(String collectionName){
		DBCollection collection = this.dbInstance.getCollection(collectionName);
		return collection;
	}

	@Override
	public void testStarted(String host) {
		this.testStarted();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MongoDBConfig [varName=").append(varName).append(", host=")
				.append(host).append(", port=").append(port).append(", dbName=")
				.append(dbName)
				.append(", username=").append(username).append(", password=")
				.append(password).append("]");
		return builder.toString();
	}

}
