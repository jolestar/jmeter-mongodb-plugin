/**
 * 
 */
package com.jolestar.jmeter.mongodb;

import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

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

	@Override
	public void sampleOccurred(SampleEvent e) {
		JMeterVariables variables = JMeterContextService.getContext().getVariables();
		Object mongo = variables.getObject(this.getMongoDBConfigName());
		if(mongo == null){
			log.error("can not find mongo db config with name:"+this.getMongoDBConfigName());
		}else{
			log.info("save result to mongodb :"+mongo.toString());
		}
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
