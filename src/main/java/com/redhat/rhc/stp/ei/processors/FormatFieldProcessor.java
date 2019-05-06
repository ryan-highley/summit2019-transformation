package com.redhat.rhc.stp.ei.processors;

import java.text.Format;
import java.text.ParseException;

import org.apache.camel.Exchange;

import com.redhat.rhc.stp.ei.CamelHelper;

/**
 * Field processor retrieving an existing field value, parsing the value into an
 * Object with a configured input java.text.Format instance, formatting the
 * parsed Object into a String with another java.text.Format instance, and
 * finally setting a new field with the formatted String.
 * 
 * @author Ryan Highley - Red Hat (u33720)
 *
 */
public class FormatFieldProcessor extends AbstractFieldProcessor {

	public FormatFieldProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// Grab the Format instances
		Format inFormat = null;
		if (inputFormatName != null)
			inFormat = CamelHelper.getCamelContextObject(inputFormatName, Format.class, exchange);

		Format outFormat = null;
		if (outputFormatName != null)
			outFormat = CamelHelper.getCamelContextObject(outputFormatName, Format.class, exchange);

		// Must have an input parser
		if (inFormat == null)
			throw new IllegalStateException(
					"Unable to parse an input value without an input parser with name " + inputFormatName);

		// Get the Message header value to be manipulated
		Object inputValue = exchange.getIn().getHeader(inputFieldName);

		// Must have an input value
		if (inputValue == null)
			throw new IllegalStateException("Unable to parse a null input value with name " + inputFieldName);

		Object inputObject = null;
		try {
			// Parse the inputValue.toString() value into an inputObject
			// If the inputValue is already a String, no big deal.
			// If not, the natural String conversion for the inputValue type
			// will be what is parsed
			inputObject = inFormat.parseObject(inputValue.toString());
		} catch (ParseException pe) {
			// Oops, better tell someone parsing failed
			throw new IllegalStateException("Unable to parse from input format " + inFormat + " with input "
					+ inputValue + " for field " + inputFieldName, pe);
		}

		// Must have an input object
		if (inputObject == null)
			throw new IllegalStateException("Unable to format a null input object from input " + inputValue
					+ " with formatter " + outFormat + " for field " + inputFieldName);

		String outputValue = null;
		if (outFormat != null) {
			// If there's a formatter Format and an inputObject, format the
			// inputObject into a String
			outputValue = outFormat.format(inputObject);
		} else {
			// If there's not a formatter Format, just use
			// inputObject.toString()
			outputValue = inputObject.toString();
		}

		// A null outputValue *should* be impossible at this point as the
		// abstract Format format() final implementation calls toString() on the
		// value returned by Format subclasses. Check anyway, just to be safe,
		// but test code coverage will miss this case.
		if (outputValue == null) {
			// Oops, better tell someone we have no formatted output value
			throw new IllegalStateException("Unable to format from output format " + outFormat + " with output "
					+ inputObject + " for field " + outputFieldName);
		}

		// Set the new field value
		exchange.getIn().setHeader(outputFieldName, outputValue);
	}

	private String inputFormatName;
	private String outputFormatName;

	/**
	 * Gets the name of the input parser Format
	 * 
	 * @return the value name of the parser Format
	 */
	public String getInputFormatName() {
		return inputFormatName;
	}

	/**
	 * Sets the name of the input parser Format
	 * 
	 * @param inputFormatName
	 *            the value name of the parser Format
	 */
	public void setInputFormatName(String inputFormatName) {
		this.inputFormatName = inputFormatName;
	}

	/**
	 * Gets the name of the output formatter Format
	 * 
	 * @return the value name of the formatter Format
	 */
	public String getOutputFormatName() {
		return outputFormatName;
	}

	/**
	 * Sets the name of the output formatter Format
	 * 
	 * @param outputFormatName
	 *            the value name of the formatter Format
	 */
	public void setOutputFormatName(String outputFormatName) {
		this.outputFormatName = outputFormatName;
	}
}
