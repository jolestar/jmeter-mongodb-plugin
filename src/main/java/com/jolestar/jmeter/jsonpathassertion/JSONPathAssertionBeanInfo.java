/**
 * 
 */
package com.jolestar.jmeter.jsonpathassertion;

import java.beans.PropertyDescriptor;

import org.apache.jmeter.testbeans.BeanInfoSupport;

/**
 * @author jolestar
 *
 */
public class JSONPathAssertionBeanInfo extends BeanInfoSupport {

	public JSONPathAssertionBeanInfo() {
		super(JSONPathAssertion.class);
		
		PropertyDescriptor p;
		p = property(JSONPathAssertion.JSONPATH);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		//p.setValue(NOT_OTHER, Boolean.TRUE);
		p.setValue(DEFAULT, "");
		
		p = property(JSONPathAssertion.JSONVALIDATION);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		//p.setValue(NOT_OTHER, Boolean.TRUE);
		p.setValue(DEFAULT,false);
		
		p = property(JSONPathAssertion.EXPECTEDVALUE);
		p.setValue(NOT_UNDEFINED, Boolean.TRUE);
		p.setValue(NOT_EXPRESSION, Boolean.TRUE);
		//p.setValue(NOT_OTHER, Boolean.TRUE);
		p.setValue(DEFAULT,"");
	}

	
}
