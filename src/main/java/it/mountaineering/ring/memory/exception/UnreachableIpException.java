package it.mountaineering.ring.memory.exception;

public class UnreachableIpException extends WebcamPropertyIDException {

	public UnreachableIpException(String exception) {
		super();
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}

}
