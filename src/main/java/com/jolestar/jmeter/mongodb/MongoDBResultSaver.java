/**
 * 
 */
package com.jolestar.jmeter.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.mongodb.BasicDBObject;
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
	public static String FIELD_RESULT_VAR_NAME = "resultVarName";
	public static String FIELD_SUCCESS_ONLY = "successOnly";
	public static String FIELD_ERRORS_ONLY = "errorsOnly";
	public static String FIELD_OBJECT_ID_NAME = "objectIdName";

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
	
	public String getResultVarName(){
		return this.getPropertyAsString(FIELD_RESULT_VAR_NAME);
	}
	
	public void setResultVarName(String resultVarName){
		this.setProperty(FIELD_RESULT_VAR_NAME, resultVarName);
	}
	
	public boolean isSuccessOnly(){
		return this.getPropertyAsBoolean(FIELD_SUCCESS_ONLY);
	}
	
	public boolean isErrorsOnly(){
		return this.getPropertyAsBoolean(FIELD_ERRORS_ONLY);
	}
	
	public String getObjectIdName(){
		return this.getPropertyAsString(FIELD_OBJECT_ID_NAME);
	}

	@Override
	public void sampleOccurred(SampleEvent e) {
		if (e.getResult().isSuccessful()){
            if (isErrorsOnly()){
                return;
            }
        } else {
            if (isSuccessOnly()){
                return;
            }
        }
		JMeterVariables variables = JMeterContextService.getContext().getVariables();
		MongoDBConfig config = (MongoDBConfig)variables.getObject(this.getMongoDBConfigName());
		if(config == null){
			log.error("can not find mongo db config with name:"+this.getMongoDBConfigName());
		}else{
			log.info("save result to mongodb :"+config.toString());
			String result = null;
			if(StringUtils.isBlank(this.getResultVarName())){
				result = e.getResult().getResponseDataAsString();
			}else{
				result = variables.get(this.getResultVarName());
			}
			if(StringUtils.isBlank(result)||"NULL".equalsIgnoreCase(result)){
				log.error("result is blank.");
			}else{
				if(log.isDebugEnabled()){
					log.debug("result:"+result);
				}
				JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
				DBCollection collection = config.getCollection(this.getMongoDBCollectionName());
				try {
					Object obj = parser.parse(result);
					saveObject(obj, collection);
				} catch (ParseException ex) {
					log.error("parse json error:"+ex.getMessage()+", responseStr:"+result);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveObject(Object obj,DBCollection collection){
		Map map = null;
		if(obj instanceof Map){
			map = (Map)obj;
			String objectIdName = this.getObjectIdName();
			if(!StringUtils.isBlank(objectIdName)){
				Object id = map.get(objectIdName);
				if(id != null){
					map = new HashMap(map);
					map.put("_id", id);
				}else{
					log.warn("can not find id by name:"+objectIdName);
				}
			}
		}else{
			map = new HashMap<String, Object>();
			map.put("result", obj);
		}
		map.put("_jmeter_update_time", new Date());
		DBObject dbObject = new BasicDBObject(map);
		collection.save(dbObject);
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
