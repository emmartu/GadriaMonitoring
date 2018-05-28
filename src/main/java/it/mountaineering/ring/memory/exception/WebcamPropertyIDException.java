package it.mountaineering.ring.memory.exception;

public class WebcamPropertyIDException extends PropertiesException {

	public WebcamPropertyIDException(String exception) {
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}
}