package com.jolestar.jmeter.mongodb.gui;

import java.awt.BorderLayout;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.reporters.ResultSaver;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractListenerGui;
import org.apache.jorphan.gui.JLabeledTextField;

import com.jolestar.jmeter.mongodb.MongoDBResultSaver;

/**
 * Create a ResultSaver test element, which saves the sample information in set
 * of files
 *
 */
public class MongoDBResultSaverGui extends AbstractListenerGui implements Clearable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 9150131463742788845L;

	private JTextField mongoConfigName;
	
	private static final String PROP_DISPLAY_NAME = "displayName";
	private static final String PROP_SHORT_DESCRIPTION = "shortDescription";
    
	/**
	 * resourceBundle need to be static. because getStaticLabel method be called at super() construct. 
	 */
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
    		MongoDBResultSaverGui.class.getName() + "Resources");

    public MongoDBResultSaverGui() {
        super();
        init();
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#getStaticLabel()
     */
    @Override
    public String getLabelResource() {
        return null;
    }
    
    

    @Override
	public String getStaticLabel() {
		return resourceBundle.getString(PROP_DISPLAY_NAME);
	}

	/**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement)
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
       
        
        this.mongoConfigName.setText(el.getPropertyAsString(MongoDBResultSaver.FIELD_MONGODB_CONFIG_NAME));
        
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    @Override
    public TestElement createTestElement() {
    	MongoDBResultSaver resultSaver = new MongoDBResultSaver();
        modifyTestElement(resultSaver);
        return resultSaver;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement te) {
        super.configureTestElement(te);
        te.setProperty(MongoDBResultSaver.FIELD_MONGODB_CONFIG_NAME, this.mongoConfigName.getText());
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();
        this.mongoConfigName.setText("");
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        box.add(createPanel());      
        add(box, BorderLayout.NORTH);
    }

    private JPanel createPanel()
    {
    	String labelStr = resourceBundle.getString(MongoDBResultSaver.FIELD_MONGODB_CONFIG_NAME+"."+PROP_DISPLAY_NAME);
        JLabel label = new JLabel(labelStr); 

        mongoConfigName = new JTextField(10);
        mongoConfigName.setName(MongoDBResultSaver.FIELD_MONGODB_CONFIG_NAME);
        String tipName = MongoDBResultSaver.FIELD_MONGODB_CONFIG_NAME+"."+PROP_SHORT_DESCRIPTION;
        if(resourceBundle.containsKey(tipName)){
        	String tip = resourceBundle.getString(tipName);
        	mongoConfigName.setToolTipText(tip);
        }
        
        label.setLabelFor(mongoConfigName);

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(mongoConfigName, BorderLayout.CENTER);
        return panel;
    }


    @Override
    public void clearData() {
    }

}
