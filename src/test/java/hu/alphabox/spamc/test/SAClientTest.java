package hu.alphabox.spamc.test;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.alphabox.spamc.SAClient;
import hu.alphabox.spamc.SACommand;
import hu.alphabox.spamc.SARequest;

public class SAClientTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SAClientTest.class);

	private static String testMessage;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testMessage = new String(Files.readAllBytes(Paths.get(SAClientTest.class.getResource("/gtube.eml").toURI())));
	}

	@Test
	public void testAllSACommand() throws Exception {
		SARequest request = new SARequest();
		request.setMessage(testMessage);
		request.useCompression(false);
		SAClient client = new SAClient(InetAddress.getByName("localhost"), 783);
		for (SACommand command : SACommand.values()) {
			LOGGER.info("Send command: " + command.name());
			if (command == SACommand.TELL) {
				request.addHeader("Message-class", "spam");
				request.addHeader("Set", "local,remote");
			}
			request.setCommand(command);
			client.sendRequest(request);
		}
	}

}
