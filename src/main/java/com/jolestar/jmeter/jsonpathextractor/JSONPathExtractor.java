/*!
 * AtlantBH Custom Jmeter Components v1.0.0
 * http://www.atlantbh.com/jmeter-components/
 *
 * Copyright 2011, AtlantBH
 *
 * Licensed under the under the Apache License, Version 2.0.
 */

package com.jolestar.jmeter.jsonpathextractor;

import java.text.ParseException;
import java.util.List;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * This is main class for JSONPath extractor which works on previous sample
 * result and extracts value from JSON output using JSONPath
 * 
 * @author Bakir Jusufbegovic / AtlantBH
 */
public class JSONPathExtractor extends AbstractTestElement implements PostProcessor,TestBean {

	private static final long serialVersionUID = 1L;
	
	static final String JSONPATH = "jsonPath";
	static final String VAR = "var";
	
	private String jsonPath;
	private String var;
	
	public JSONPathExtractor() {
		super();
	}
	
	
	
	public String getJsonPath() {
		return jsonPath;
	}



	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}



	public String getVar() {
		return var;
	}



	public void setVar(String var) {
		this.var = var;
	}



	public String extractJSONPath(String jsonString, String jsonPath) throws Exception {
		Object jsonPathResult = JsonPath.read(jsonString, jsonPath);
		if (null == jsonPathResult) {
			throw new Exception("Invalid JSON path provided!");
		} else if (jsonPathResult instanceof List && ((List<?>)jsonPathResult).isEmpty()) {
			return "NULL";
		}else{
			return jsonPathResult.toString();
		}
	}
	
	@Override
	public void process() {
		JMeterContext context = getThreadContext();
		
		final SampleResult previousResult = context.getPreviousResult();
		if (previousResult == null) {
			return;
		}
		
		JMeterVariables vars = context.getVariables();
		String responseData = context.getPreviousResult().getResponseDataAsString();
		
		try 
		{
			String response = this.extractJSONPath(responseData, this.getJsonPath());
			vars.put(this.getVar(), response);
		} 
		catch (InvalidPathException e) 
		{
			System.out.println("Extract failed!. Invalid JSON path: " + e.getMessage());
		} 
		catch (ParseException e) 
		{
			System.out.println("Extract failed!. Invalid JSON path: " + e.getMessage());
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}	
	}
}
