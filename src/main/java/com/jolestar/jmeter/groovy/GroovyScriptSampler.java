package com.jolestar.jmeter.groovy;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;

/**
 * Document me.
 * <br>
 * User: Matthew McCullough, matthewm@ambientideas.com
 * Date: Nov 10, 2008
 */
public class GroovyScriptSampler extends AbstractSampler implements TestBean {

	private static final String UTILITYSCRIPTFILENAME = "GroovyScriptSampler.utilityScriptFilename"; //$NON-NLS-1$    
	private static final String SETUPSCRIPTFILENAME = "GroovyScriptSampler.setupScriptFilename"; //$NON-NLS-1$
	private static final String PRIMARYSCRIPTFILENAME = "GroovyScriptSampler.primaryScriptFilename"; //$NON-NLS-1$

	public String getUtilityScriptFilename() {
        return getPropertyAsString(UTILITYSCRIPTFILENAME);
    }

    public void setUtilityScriptFilename(String newUtilityScriptFilename) {
        this.setProperty(UTILITYSCRIPTFILENAME, newUtilityScriptFilename);
    }

    public String getSetupScriptFilename() {
        return getPropertyAsString(SETUPSCRIPTFILENAME);
    }

    public void setSetupScriptFilename(String newSetupScriptFilename) {
        this.setProperty(SETUPSCRIPTFILENAME, newSetupScriptFilename);
    }

	public String getPrimaryScriptFilename() {
        return getPropertyAsString(PRIMARYSCRIPTFILENAME);
    }

    public void setPrimaryScriptFilename(String newPrimaryScriptFilename) {
        this.setProperty(PRIMARYSCRIPTFILENAME, newPrimaryScriptFilename);
    }

    public SampleResult sample(Entry e) {
        final String label = getName();
		final String utilityScriptFilename = getUtilityScriptFilename();
		final String setupScriptFilename = getSetupScriptFilename();
        final String primaryScriptFilename = getPrimaryScriptFilename();

        SampleResult result = new SampleResult();
        result.setSampleLabel(label);

        result.setResponseCode("200"); // $NON-NLS-1$
        result.setResponseMessage("OK"); // $NON-NLS-1$
        result.setSuccessful(true);
        result.setDataType(SampleResult.TEXT);

        //Run the groovy script
        try {
            //Get the classloader of JMeter, where we have put all the Groovy JARs
            ClassLoader parent = getClass().getClassLoader();
            GroovyClassLoader loader = new GroovyClassLoader(parent);

			//Call the utility groovy script (not timed) first, if it exists
			if (utilityScriptFilename != null && utilityScriptFilename.length() > 0) {
				runGroovyScript(loader, utilityScriptFilename);
			}

			//Call the setup groovy script (not timed) first, if it exists
			if (setupScriptFilename != null && setupScriptFilename.length() > 0) {
				runGroovyScript(loader, setupScriptFilename);
			}
			
			//Start the timing
			result.sampleStart();
			//Run the core script
            runGroovyScript(loader, primaryScriptFilename);
        }
        catch (Throwable ex) {
			String stackTrace = buildStackTraceString(ex);
			System.out.println("Groovy exception: " + stackTrace);
            //Problem encountered, so set the error statuses into the result
            result.setSuccessful(false);
            result.setResponseCode("500"); // $NON-NLS-1$
            result.setResponseMessage(stackTrace);
        }
        finally {
            result.sampleEnd();
        }

        return result;
    }

	public static void runGroovyScript(GroovyClassLoader loader, String scriptFilename) throws Exception {
		System.out.println("Sampler loading groovy file: " + scriptFilename);
        Class<?> groovyClass = loader.parseClass(new File(scriptFilename));

        //Call the "run" method on an instance
        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
        Object[] args = {};
        groovyObject.invokeMethod("run", args);
	}
	
    public static String buildStackTraceString(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
