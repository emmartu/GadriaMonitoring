package it.mountaineering.ring.memory.exception;

public class WebcamPropertyIDException extends PropertiesException {

	public WebcamPropertyIDException() {
		super();
	}

	public WebcamPropertyIDException(String exception) {
		super();
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}
}