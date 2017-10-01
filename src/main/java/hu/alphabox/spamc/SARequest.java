package hu.alphabox.spamc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SARequest {
	
	public static final String PROTOCOL_VERSION = "1.5";
	public static final String NEW_LINE = "\r\n";
	public static final String ENCODING = "UTF-8";

	private SACommand command;

	private String email;
	
	private boolean useCompression;

	private boolean message;

	private Map<String, Object> headers;

	public SARequest() {
		this("nobody");
	}
	
	public SARequest(String user) {
		this.useCompression = false;
		this.message = false;
		this.headers = new HashMap<>();
		this.setUser(user);
	}
	
	public void addHeader(String key, Object value) {
		headers.put(key, value);
	}
	
	public void removeHeader(String key) {
		headers.remove(key);
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

	public void useCompression(boolean useCompression) {
		this.useCompression = useCompression;
	}
	
	public boolean isUseCompression() {
		return this.useCompression;
	}

	public String getHeaders() {
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, Object> entry : headers.entrySet()) {
			buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append(NEW_LINE);
		}
		return buffer.toString();
	}

	public ByteArrayOutputStream getRequestByteArray() throws IOException {
		String requestProtocol = getCommand().name() + ' ' + "SPAMC/" + PROTOCOL_VERSION + NEW_LINE;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(requestProtocol.getBytes(ENCODING));
		if ( useCompression ) {
			headers.put("Compress", "zlib");
		}
		byte[] byteEmail = useCompression ? Zlib.compress(email.getBytes(ENCODING)) : email.getBytes(ENCODING);
		setContentLength(byteEmail.length);
		outputStream.write(getHeaders().getBytes(ENCODING));
		outputStream.write(NEW_LINE.getBytes(ENCODING));
		outputStream.write(byteEmail);
		return outputStream;
		
	}

}
