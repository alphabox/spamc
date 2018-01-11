package hu.alphabox.spamc;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws SAException, IOException {
		String message = "Subject: Test spam mail (GTUBE)\n" + 
				"Message-ID: <GTUBE1.1010101@example.net>\n" + 
				"Date: Wed, 23 Jul 2003 23:30:00 +0200\n" + 
				"From: Sender <sender@example.net>\n" + 
				"To: Recipient <recipient@example.net>\n" + 
				"Precedence: junk\n" + 
				"MIME-Version: 1.0\n" + 
				"Content-Type: text/plain; charset=us-ascii\n" + 
				"Content-Transfer-Encoding: 7bit\n" + 
				"\n" + 
				"This is the GTUBE, the\n" + 
				"	Generic\n" + 
				"	Test for\n" + 
				"	Unsolicited\n" + 
				"	Bulk\n" + 
				"	Email\n" + 
				"\n" + 
				"If your spam filter supports it, the GTUBE provides a test by which you\n" + 
				"can verify that the filter is installed correctly and is detecting incoming\n" + 
				"spam. You can send yourself a test mail containing the following string of\n" + 
				"characters (in upper case and with no white spaces and line breaks):\n" + 
				"\n" + 
				"XJS*C4JDBQADN1.NSBN3*2IDNEN*GTUBE-STANDARD-ANTI-UBE-TEST-EMAIL*C.34X\n" + 
				"\n" + 
				"You should send this test mail from an account outside of your network.\n";
				
		SARequest request = new SARequest();
		request.setMessage(message);
		//request.useCompression(true);
		SAClient client = new SAClient(InetAddress.getByName("10.0.10.20"), 783);
		for ( SACommand command : SACommand.values() ) {
			LOGGER.info("Send command: " + command.name());
			if ( command == SACommand.TELL ) {
				request.addHeader("Message-class", "spam");
				request.addHeader("Set", "local,remote");
			}
			request.setCommand(command);			
			client.sendRequest(request);
		}	
	}

}
