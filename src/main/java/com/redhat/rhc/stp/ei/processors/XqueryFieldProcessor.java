package com.redhat.rhc.stp.ei.processors;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.xquery.XQueryBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import com.redhat.rhc.stp.ei.CamelHelper;

import io.syndesis.extension.api.annotations.Action;
import io.syndesis.extension.api.annotations.ConfigurationProperty;

/**
 * Field processor setting new field values based on XQuery values executed against the
 * Camel Exchange. This is basically a convenience Processor for
 * <code>
 * &lt;setHeader headerName="<i>outputFieldName</i>"&gt;&lt;xquery&gt;<i>xquery</i>&lt;/xquery&gt;&lt;/setHeader&gt;
 * </code>
 */
@Action(
	    id = "xquery-field-processor",
	    name = "XQuery Field Processor",
	    description = "Process the overall input with an XQuery expression and assign the result value to the output field"
	)
public class XqueryFieldProcessor extends AbstractFieldProcessor {

	public XqueryFieldProcessor() {
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// Process the XQuery against the Exchange just like Camel does with an XQueryBuilder
		// Since this boils down to a proper Camel XQuery expression, if it's valid in
		// Camel's (Saxon) XQuery implementation, it's valid here.
		String fieldValue = CamelHelper.processXQuery(xqueryBuilder, exchange);

		// Set the new field
		exchange.getIn().setHeader(outputFieldName, fieldValue);
	}

	public String processField(Object inputValue) {
		CamelContext context = new DefaultCamelContext();
		Exchange exchange = new ExchangeBuilder(context).withBody(inputValue).build();

		String fieldValue;
		try {
			process(exchange);
			fieldValue = exchange.getIn().getHeader(outputFieldName, String.class);
			
			// Just in case the exchange has an exception that wasn't thrown,
			// handle it as if it had been. This will fail code coverage since
			// causing this condition externally is not possible.
			if(exchange.getException() != null)
				throw exchange.getException();
		} catch(IllegalStateException ise) {
			if(ise.getCause() != null) {
				fieldValue = ise.getCause().getMessage();
			} else {
				// Any IllegalStateException thrown by CamelHelper.processXQuery
				// will have a cause. This handles anything else in Camel XQuery
				// processing throwing an IllegalStateException. This case will
				// be missed by code coverage.
				fieldValue = ise.getMessage();
			}
		} catch(Exception e) {
			fieldValue = e.getMessage();
		}
		
		return fieldValue;
	}
	
    @ConfigurationProperty(
            name = "xquery",
            displayName = "XQuery",
            description = "XQuery resulting in output field value for this step")
	private String xquery;
	private XQueryBuilder xqueryBuilder;
	
	/**
	 * Gets the XQuery to be executed
	 * @return the XQuery configuration as a String
	 */
	public String getXquery() {
		return xquery;
	}

	/**
	 * Sets the XQuery to be executed
	 * @param xquery the XQuery configuration as a String
	 */
	public void setXquery(String xquery) {
		this.xquery = xquery;
		this.xqueryBuilder = CamelHelper.createXQueryBuilder(xquery);
	}
}
