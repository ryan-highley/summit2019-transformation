package com.redhat.rhc.stp.ei;

import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.component.xquery.XQueryBuilder;
import org.apache.camel.spi.Registry;

/**
 * Convenience methods for interacting with Camel
 * 
 * @author Ryan Highley - Red Hat (u33720)
 *
 */
public final class CamelHelper {

	// This is a true utility method class--private no-arg constructor and final
	// class
	private CamelHelper() {
	}

	/**
	 * Gets an object from the CamelContext's Registry for the current Exchange.
	 * 
	 * @param objectName
	 *            the name of the object in the Registry
	 * @param objectClass
	 *            the object's class
	 * @param exch
	 *            the Exchange to use to get the CamelContext and Registry
	 * @return the object from the Registry, or null if it could not be found
	 */
	public static <T> T getCamelContextObject(String objectName, Class<T> objectClass, Exchange exch) {
		Registry registry = exch.getContext().getRegistry();

		return registry.lookupByNameAndType(objectName, objectClass);
	}

	/**
	 * Creates a Camel XQueryBuilder from an XQuery String definition. Since
	 * this method returns a proper Camel XQueryBuilder, any XQuery definition
	 * valid in Camel is also valid here.
	 * 
	 * @param xquery
	 *            the XQuery String definition
	 * @return a Camel XQueryBuilder compiled from the XQuery String definition
	 */
	public static XQueryBuilder createXQueryBuilder(String xquery) {
		return XQueryBuilder.xquery(xquery);
	}

	/**
	 * Processes the XQueryBuilder against the Exchange, evaluating the XQuery
	 * result as a String
	 * 
	 * @param xqueryBuilder
	 *            the Camel XQueryBuilder to process
	 * @param exch
	 *            the Camel Exchange against which the XQueryBuilder is
	 *            processed
	 * @return the String result of the XQuery
	 * @throws IllegalStateException
	 *             if the XQueryBuilder processing failed
	 */
	public static String processXQuery(XQueryBuilder xqueryBuilder, Exchange exch) throws IllegalStateException {
		try {
			return xqueryBuilder.evaluateAsString(exch);
		} catch (Exception e) {
			throw new IllegalStateException("Unable to process xquery against Exchange", e);
		}
	}

	/**
	 * Gets an object from the CamelContext's Registry for the current Exchange.
	 * 
	 * @param objectClass
	 *            the object's class
	 * @param exch
	 *            the Exchange to use to get the CamelContext and Registry
	 * @return the object from the Registry, or null if it could not be
	 *         found--if multiple objects exist, only one is returned
	 */
	public static <T> T getCamelContextObjectByType(Class<T> objectClass, Exchange exch) {
		Registry registry = exch.getContext().getRegistry();

		Set<T> objectSet = registry.findByType(objectClass);
		// Camel Registry instances should always return an empty Set when
		// findByType doesn't find any matching instances--doesn't mean one
		// won't. Unit test coverage will miss the "objectSet == null" branch
		if (objectSet == null || objectSet.isEmpty())
			return null;

		return objectSet.iterator().next();
	}
	
	public static void testCoverageForConstructor() {
		new CamelHelper();
	}
}
