package hu.alphabox.spamc;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args) throws UnknownHostException, SAException {
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
		request.setEmail(message);
		request.setUser("nobody");
		request.setUseCompression(true);
		SAClient client = new SAClient(InetAddress.getByName("192.168.1.104"), 783);
		for ( SACommand command : SACommand.values() ) {
			System.out.println("Send command: " + command.name());
			request.setCommand(command);			
			client.sendRequest(request);
		}
	}

}
