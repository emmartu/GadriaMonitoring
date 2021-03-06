package it.mountaineering.ring.memory.exception;

public class NumberFormatPropertiesException extends PropertiesException {

	public NumberFormatPropertiesException(String exception) {
		super();
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}
}