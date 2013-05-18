/**
 * 
 */
package com.jolestar.jmeter.mongodb;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;


/**
 * @author jolestar
 * 
 */
public class MongoDBConfigBeanInfo extends BeanInfoSupport {

	public static enum Field {
		varName("mongodb"), host("localhost"), port(27017), dbName("test"),username(""), password("");
		private Object defaultValue;

		private Field(Object defaultValue) {
			this.defaultValue = defaultValue;
		}

		public Object defaultValue() {
			return defaultValue;
		}
	}
	
	public MongoDBConfigBeanInfo() {
		super(MongoDBConfig.class);
		
		createPropertyGroup("mongo_db_config_var", new String[]{Field.varName.name()});
		createPropertyGroup("mongo_db_config", new String[]{Field.host.name(),Field.port.name(),Field.dbName.name(),Field.username.name(),Field.password.name()});
		//createPropertyGroup("Mongo Server auth", new String[]{Field.username.name(),Field.password.name()});
		
		PropertyDescriptor p;
		for (Field f : Field.values()) {
			p = property(f.name());
			p.setValue(NOT_UNDEFINED, Boolean.TRUE);
			p.setValue(DEFAULT, f.defaultValue());
			p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		}
		
	}

}
