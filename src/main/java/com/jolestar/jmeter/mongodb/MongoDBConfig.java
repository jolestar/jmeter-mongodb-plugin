package com.jolestar.jmeter.mongodb;

import java.net.UnknownHostException;

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
	private transient String db;
	private transient String collection;
	private transient String username;
	private transient String password;

	

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

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
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
	


	@Override
	public void testEnded() {
		// TODO close mongodb
		log.info("close mongo db :" + this.varName);
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
		log.info("create mongo db config :" + this.toString());
		
		try {
			DB db = Mongo.connect(new DBAddress(this.getHost(), this.getPort(), this.getDb()));
			DBCollection collection = db.getCollection(this.getCollection());
			variables.putObject(varName, collection);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void testStarted(String host) {
		this.testStarted();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MongoDBConfig [varName=").append(varName).append(", host=")
				.append(host).append(", port=").append(port).append(", db=")
				.append(db).append(", collection=").append(collection)
				.append(", username=").append(username).append(", password=")
				.append(password).append("]");
		return builder.toString();
	}

}
