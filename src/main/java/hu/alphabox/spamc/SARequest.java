package hu.alphabox.spamc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class SARequest {

	private SACommand command;

	private String email;
	
	private boolean useCompression;

	// TODO: DeflaterOutputStream (zlib compress is missing)
	private boolean message;

	private Map<String, Object> headers;

	public SARequest() {
		this.useCompression = false;
		this.message = false;
		this.headers = new HashMap<>();
		this.setUser("nobody");
	}
	
	private String getCompressedMessage(byte[] message) {
		Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DeflaterOutputStream outputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater, true);
		try {
			outputStream.write(message, 0, message.length);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArrayOutputStream.toString();
	}
	
	private void setContentLength(int length) {
		headers.put("Content-length", length);
	}
	
	private void removeContentLengt() {
		headers.remove("Content-length");
	}

	public SACommand getCommand() {
		return command;
	}

	public void setUser(String user) {
		headers.put("User", user);
	}

	public String getUser() {
		return headers.containsKey("User") ? (String) headers.get("User") : null;
	}

	public void setCommand(SACommand command) {
		this.command = command;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		if (email != null) {
			this.message = true;
			setContentLength(email.length());
		} else {
			removeContentLengt();
			this.message = false;
		}
	}

	public boolean hasMessage() {
		return message;
	}

	public void setMessage(boolean message) {
		this.message = message;
	}
	
	

	public void setUseCompression(boolean useCompression) {
		this.useCompression = useCompression;
	}

	public String getHeaders() {
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, Object> entry : headers.entrySet()) {
			buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
		}
		return buffer.toString();
	}
	
	public String getRequest() {
		StringBuilder builder = new StringBuilder();
		String message = email;
		if (useCompression) {
			headers.put("Compress", "zlib");
			message = getCompressedMessage(email.getBytes());
		}
		setContentLength(message.length());
		builder.append(getHeaders()).append("\r\n").append(message);
		return builder.toString();
	}

}
