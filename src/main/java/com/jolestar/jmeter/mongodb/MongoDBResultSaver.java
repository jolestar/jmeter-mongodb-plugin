/**
 * 
 */
package com.jolestar.jmeter.mongodb;

import java.util.HashMap;
import java.util.Map;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * @author jolestar
 *
 */
public class MongoDBResultSaver extends AbstractTestElement implements SampleListener {
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggingManager.getLoggerForClass();
	
	
	public static String FIELD_MONGODB_CONFIG_NAME = "mongoDBConfigName";
	public static String FIELD_MONGODB_COLLECTION_NAME = "mongoDBCollectionName";

    public MongoDBResultSaver() {
        super();
    }

    /*
     * Constructor for use during startup (intended for non-GUI use) @param name
     * of summariser
     */
    public MongoDBResultSaver(String name) {
        this();
        setName(name);
    }

    @Override
    public void clear() {
        super.clear();
    }
    

	public String getMongoDBConfigName() {
		return this.getPropertyAsString(FIELD_MONGODB_CONFIG_NAME);
	}

	public void setMongoDBConfigName(String mongoDBConfigName) {
		this.setProperty(FIELD_MONGODB_CONFIG_NAME, mongoDBConfigName);
	}
	
	public String getMongoDBCollectionName(){
		return this.getPropertyAsString(FIELD_MONGODB_COLLECTION_NAME);
	}
	
	public void setMongoDBCollectionName(String mongoDBCollectionName){
		this.setProperty(FIELD_MONGODB_COLLECTION_NAME, mongoDBCollectionName);
	}

	@Override
	public void sampleOccurred(SampleEvent e) {
		JMeterVariables variables = JMeterContextService.getContext().getVariables();
		MongoDBConfig config = (MongoDBConfig)variables.getObject(this.getMongoDBConfigName());
		if(config == null){
			log.error("can not find mongo db config with name:"+this.getMongoDBConfigName());
		}else{
			log.info("save result to mongodb :"+config.toString());
			String str = e.getResult().getResponseDataAsString();
			JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			DBCollection collection = config.getCollection(this.getMongoDBCollectionName());
			try {
				Object obj = parser.parse(str);
				saveObject(obj, collection);
			} catch (ParseException ex) {
				log.error("parse json error:"+ex.getMessage()+", responseStr:"+str);
			}
		}
	}
	
	private void saveObject(Object obj,DBCollection collection){
		Map map = null;
		if(obj instanceof Map){
			map = (Map)obj;
		}else{
			map = new HashMap<String, Object>();
			map.put("result", obj);
		}
		DBObject dbObject = new BasicDBObject(map);
		collection.insert(dbObject);
	}

	@Override
	public void sampleStarted(SampleEvent arg0) {
		//do nothing
	}

	@Override
	public void sampleStopped(SampleEvent arg0) {
		//do nothing
	}
	
	

}
