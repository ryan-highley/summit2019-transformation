package com.redhat.rhc.stp.ei.processors.format;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Currency;

public class NumberFieldFormat extends DecimalFormat {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4164420256760705611L;

	// Everything we need is supported by DecimalFormat with different
	// configurable properties--just go ahead with the typecast here so if
	// NumberFormat.getInstance() returns something else (EXTREMELY UNLIKELY),
	// instantiation will fail immediately.
	private final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();

	public NumberFieldFormat() {
	}

	@Override
	public synchronized StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		return format.format(number, toAppendTo, pos);
	}

	@Override
	public synchronized StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		return format.format(number, toAppendTo, pos);
	}

	@Override
	public synchronized Number parse(String source, ParsePosition parsePosition) {
		return format.parse(source, parsePosition);
	}

	public synchronized void setGroupingSize(String groupingSize) {
		format.setGroupingSize(Integer.parseInt(groupingSize));
	}

	public synchronized void setMaximumFractionDigits(String maximumFractionDigits) {
		format.setMaximumFractionDigits(Integer.parseInt(maximumFractionDigits));
	}

	public synchronized void setMaximumIntegerDigits(String maximumIntegerDigits) {
		format.setMaximumIntegerDigits(Integer.parseInt(maximumIntegerDigits));
	}

	public synchronized void setMinimumFractionDigits(String minimumFractionDigits) {
		format.setMinimumFractionDigits(Integer.parseInt(minimumFractionDigits));
	}

	public synchronized void setMinimumIntegerDigits(String minimumIntegerDigits) {
		format.setMinimumIntegerDigits(Integer.parseInt(minimumIntegerDigits));
	}

	public synchronized void setMultiplier(String multiplier) {
		format.setMultiplier(Integer.parseInt(multiplier));
	}

	public synchronized void setNegativePrefix(String negativePrefix) {
		format.setNegativePrefix(negativePrefix);
	}

	public synchronized void setNegativeSuffix(String negativeSuffix) {
		format.setNegativeSuffix(negativeSuffix);
	}

	public synchronized void setPositivePrefix(String positivePrefix) {
		format.setPositivePrefix(positivePrefix);
	}

	public synchronized void setPositiveSuffix(String positiveSuffix) {
		format.setPositiveSuffix(positiveSuffix);
	}

	public synchronized void setRoundingMode(String roundingMode) {
		if(roundingMode != null && !roundingMode.isEmpty())
			format.setRoundingMode(RoundingMode.valueOf(roundingMode));
	}

	public synchronized void setDecimalSeparatorAlwaysShown(String decimalSeparatorAlwaysShown) {
		format.setDecimalSeparatorAlwaysShown(Boolean.valueOf(decimalSeparatorAlwaysShown));
	}

	public synchronized void setGroupingUsed(String groupingUsed) {
		format.setGroupingUsed(Boolean.valueOf(groupingUsed));
	}

	public synchronized void setParseIntegerOnly(String parseIntegerOnly) {
		format.setParseIntegerOnly(Boolean.valueOf(parseIntegerOnly));
	}
	
	public synchronized void setParseBigDecimal(String parseBigDecimal) {
		format.setParseBigDecimal(Boolean.valueOf(parseBigDecimal));
	}
	
	public synchronized void setCurrency(String currencyCode) {
		format.setCurrency(Currency.getInstance(currencyCode));
	}
}
