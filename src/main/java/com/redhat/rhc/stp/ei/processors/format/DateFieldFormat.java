package com.redhat.rhc.stp.ei.processors.format;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldFormat extends DateFormat {

	private static final long serialVersionUID = -9150940060306401064L;

	private String formatString;
	private SimpleDateFormat format = new SimpleDateFormat();
	
	public DateFieldFormat() {
	}

	public synchronized String getFormatString() {
		return formatString;
	}

	public synchronized void setFormatString(String formatString) {
		this.formatString = formatString;
		this.format = new SimpleDateFormat(formatString);
	}

	@Override
	public synchronized StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return format.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public synchronized Date parse(String source, ParsePosition pos) {
		return format.parse(source, pos);
	}
}
