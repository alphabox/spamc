package hu.alphabox.spamc.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import hu.alphabox.spamc.SACommand;
import hu.alphabox.spamc.SARequest;

public class SARequestTest {

	@Test(expected = NullPointerException.class)
	public void testRequestedBytArray_With_No_Parameters() throws IOException {
		SARequest saRequest = new SARequest();
		saRequest.getRequestByteArray();
	}

	@Test
	public void testRequestedBytArray_WithOut_Email() throws IOException {
		SARequest saRequest = new SARequest();
		saRequest.setCommand(SACommand.CHECK);
		saRequest.setEmail("");
		try (ByteArrayOutputStream baos = saRequest.getRequestByteArray()) {
			String result = "CHECK SPAMC/1.5\r\n" + "User: nobody\r\n" + "Content-length: 0\r\n\r\n";
			Assert.assertEquals(result, baos.toString(StandardCharsets.UTF_8.name()));
		}
	}
}
