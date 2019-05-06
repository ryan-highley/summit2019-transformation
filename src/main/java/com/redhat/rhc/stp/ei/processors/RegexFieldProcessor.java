package com.redhat.rhc.stp.ei.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

/**
 * Field processor retrieving an existing field value, finding the configured java.util.regex.Pattern
 * in the value, and setting a new field value using either the entire found match or the first capture
 * group, if one or more regular expression capture groups are configured.
 */
@Action(
	    id = "regex-field-processor",
	    name = "RegEx Field Processor",
	    description = "Process the input field value with the regular expression and assign the result to the output field"
	)
public class RegexFieldProcessor extends AbstractFieldProcessor {

	public RegexFieldProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// Get the existing field value
		Object inputValue = exchange.getIn().getHeader(inputFieldName);

		// No value, no processing
		if(inputValue == null)
			return;

		// Get the processed field value
		String fieldValue = processField(inputValue);
		
		// Set the new field value
		exchange.getIn().setHeader(outputFieldName, fieldValue);
	}

	public String processField(Object inputValue) {
		// Execute the Pattern on the inputValue.toString() result
		Matcher inputMatcher = pattern.matcher(inputValue.toString());
		// If no match is found, tell someone
		if(!inputMatcher.find()) {
			throw new IllegalStateException("Unable to find pattern " + pattern + " against input value " + inputValue + " for field " + inputFieldName);
		}

		// To start, use the entire match just in case no capture groups are configured 
		String fieldValue = inputMatcher.group();
		
		// If there are one or more capture groups, use the first one's value instead
		if(inputMatcher.groupCount() >= 1) {
			fieldValue = inputMatcher.group(1);
		}
		
		return fieldValue;
	}
	
    @ConfigurationProperty(
            name = "pattern",
            displayName = "Pattern",
            description = "Regular expression processing input field resulting in output field value for this step")
	private String rawPattern;
	private Pattern pattern;
	
	/**
	 * Gets the java.util.regex.Pattern regular expression pattern string
	 * @return the regular expression pattern
	 */
	public String getPattern() {
		return rawPattern;
	}

	/**
	 * Sets the java.util.regex.Pattern regular expression pattern string. This value is
	 * compiled into a Pattern so any pattern description issues are handled when this
	 * property is set.  
	 * @param pattern the regular expression pattern
	 */
	public void setPattern(String pattern) {
		this.rawPattern = pattern;
		this.pattern = Pattern.compile(pattern);
	}
}
