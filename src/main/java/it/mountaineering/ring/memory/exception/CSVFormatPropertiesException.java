package it.mountaineering.ring.memory.exception;

public class CSVFormatPropertiesException extends PropertiesException {

	public CSVFormatPropertiesException(String exception) {
		super();
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}

}
