package it.mountaineering.ring.memory.exception;

public class BooleanStringPropertyException extends WebcamPropertyIDException {

	public BooleanStringPropertyException(String exception) {
		super();
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}

}
