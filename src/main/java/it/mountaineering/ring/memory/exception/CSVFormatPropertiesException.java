package it.mountaineering.ring.memory.exception;

public class CSVFormatPropertiesException extends PropertiesException {

	public CSVFormatPropertiesException(String exception) {
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}

}
