/**
 * 
 */
package com.jolestar.jmeter.jsonpathextractor;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;

/**
 * @author jolestar
 * 
 */
public class JSONPathExtractorBeanInfo extends BeanInfoSupport {

	protected JSONPathExtractorBeanInfo() {
		super(JSONPathExtractor.class);
		PropertyDescriptor p;
		p = property(JSONPathExtractor.JSONPATH);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		//p.setValue(NOT_OTHER, Boolean.TRUE);
		p.setValue(DEFAULT, "");
		
		p = property(JSONPathExtractor.VAR);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		//p.setValue(NOT_OTHER, Boolean.TRUE);
		p.setValue(DEFAULT, "");
	}

}
