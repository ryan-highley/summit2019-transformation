package com.redhat.rhc.stp.ei.processors;

import org.apache.camel.Exchange;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

/**
 * Field processor always setting an output field to the same configurable constant value.
 */
@Action(
	    id = "constant-field-processor",
	    name = "Constant Field Processor",
	    description = "Assign a constant value to the output field"
	)
public class ConstantFieldProcessor extends AbstractFieldProcessor {

	public ConstantFieldProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// Just write the field--easy, peasy
		exchange.getIn().setHeader(outputFieldName, constant);
	}

	@ConfigurationProperty(
			name="constant",
			displayName="Constant",
			description="Output field value for this step")
	private String constant;

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}
}
