package it.mountaineering.ring.memory.exception;

import it.mountaineering.ring.memory.mail.sender.MailSender;
import it.mountaineering.ring.memory.main.Main;

public class PropertiesException extends Exception {

	MailSender mailSender = new MailSender();
	
	protected String exceptionPrefixString = "Exception occured reading properties File.";
	protected String exceptionSuffixString = "Timer Task Has Been Stopped";

	public PropertiesException() {
		if(Main.vlcLauncher.isHasStarted()) {
			Main.timer.cancel();
			Main.timer.purge();
			
			exceptionSuffixString = "";
		}
	}

	public PropertiesException(String exception) {		
		mailSender.sendPropertiesExceptionEmail(exceptionPrefixString, exception, exceptionSuffixString);
	}

}
