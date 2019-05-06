package com.redhat.rhc.stp.ei.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.syndesis.extension.api.annotations.ConfigurationProperty;

/**
 * Abstract class for field processors providing properties for an inputFieldName
 * and an outputFieldName. Not all field processors use both properties during
 * processing so this is more a convenient standardization for property names.
 */
public abstract class AbstractFieldProcessor implements Processor {

	public AbstractFieldProcessor() {
	}

	@Override
	public abstract void process(Exchange exchange) throws Exception;

	@ConfigurationProperty(
			name="inputFieldName",
			displayName="Input Field Name",
			description="Field name of the input value for this step")
	protected String inputFieldName;

	@ConfigurationProperty(
			name="outputFieldName",
			displayName="Output Field Name",
			description="Field name of the output value for this step")
	protected String outputFieldName;

	public String getInputFieldName() {
		return inputFieldName;
	}

	public void setInputFieldName(String inputFieldName) {
		this.inputFieldName = inputFieldName;
	}

	public String getOutputFieldName() {
		return outputFieldName;
	}

	public void setOutputFieldName(String outputFieldName) {
		this.outputFieldName = outputFieldName;
	}
}
