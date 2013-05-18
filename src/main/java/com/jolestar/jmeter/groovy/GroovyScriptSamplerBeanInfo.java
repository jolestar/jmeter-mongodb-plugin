package com.jolestar.jmeter.groovy;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.FileEditor;
import java.beans.PropertyDescriptor;

/**
 * This class defines the bean properties that will be visually exposed for this JMeter plugin.
 * <p/>
 * User: Matthew McCullough
 * Date: Nov 10, 2008
 */
public class GroovyScriptSamplerBeanInfo extends BeanInfoSupport {
    public GroovyScriptSamplerBeanInfo() {
        super(GroovyScriptSampler.class);

        createPropertyGroup("groovy_script_untimed", new String[]{"utilityScriptFilename", "setupScriptFilename"});

		PropertyDescriptor usfP = property("utilityScriptFilename");
        usfP.setValue(NOT_UNDEFINED, Boolean.FALSE);
        usfP.setValue(DEFAULT, "");
        usfP.setValue(NOT_EXPRESSION, Boolean.TRUE);
        usfP.setPropertyEditorClass(FileEditor.class);

		PropertyDescriptor ssfP = property("setupScriptFilename");
        ssfP.setValue(NOT_UNDEFINED, Boolean.FALSE);
        ssfP.setValue(DEFAULT, "");
        ssfP.setValue(NOT_EXPRESSION, Boolean.TRUE);
        ssfP.setPropertyEditorClass(FileEditor.class);


		createPropertyGroup("groovy_script_timed", new String[]{"primaryScriptFilename"});

        PropertyDescriptor psfP = property("primaryScriptFilename");
        psfP.setValue(NOT_UNDEFINED, Boolean.TRUE);
        psfP.setValue(DEFAULT, "yourprimaryscriptnamegoeshere.groovy");
        psfP.setValue(NOT_EXPRESSION, Boolean.TRUE);
        psfP.setPropertyEditorClass(FileEditor.class);
    }
}
