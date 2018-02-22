package hu.alphabox.spamc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Store the clients request, and format it to byte array for SpamAssassin
 * server. The {@code SARequest} store the message and the {@link SACommand}
 * which we want to send to the server. It contains the protocol version, and we
 * can set the compression if we want.
 * 
 * @author Daniel Mecsei
 *
 */
public class SARequest {

	/**
	 * SpamAssassin protocol version.
	 */
	public static final String PROTOCOL_VERSION = "1.5";

	/**
	 * The SpamAssassin always send the new line endings as is.
	 */
	public static final String NEW_LINE = "\r\n";

	/**
	 * The message encoding type, which is currently not adjustable.
	 */
	public static final String ENCODING = "UTF-8";

	/**
	 * 
	 */
	private SACommand command;

	/**
	 * Store the message (email) itself.
	 */
	private String message;

	/**
	 * Send the message as zlib compressed or not.
	 */
	private boolean useCompression;

	/**
	 * The request has a message or not.
	 */
	private boolean containMessage;

	/**
	 * Store the request headers.
	 */
	private Map<String, Object> headers;

	/**
	 * Create a new {@code SARequest} object with nobody user as default setting. It
	 * is not compress message as a default setting too.
	 */
	public SARequest() {
		this("nobody");
	}

	/**
	 * Create a new {@code SARequest} object with adjusted {@code user}. It is not
	 * compress message as a default setting.
	 * 
	 * @param user
	 *            username of the user on whose behalf this scan is being performed
	 */
	public SARequest(String user) {
		this.useCompression = false;
		this.containMessage = false;
		this.headers = new HashMap<>();
		this.setUser(user);
	}

	/**
	 * Add a new header for SpamAssassin request message.
	 * 
	 * @param name
	 *            the field (header) name
	 * @param value
	 *            the field (header) value
	 */
	public void addHeader(String name, Object value) {
		headers.put(name, value);
	}

	/**
	 * Remove a header from SpamAssassin request by name.
	 * 
	 * @param name
	 *            the name of the header
	 */
	public void removeHeader(String name) {
		headers.remove(name);
	}

	/**
	 * Add a new header for SpamAssassin request to the server can determine the
	 * length of the content.
	 * 
	 * @param length
	 *            the length of the content (message)
	 */
	private void setContentLength(int length) {
		headers.put("Content-length", length);
	}

	/**
	 * It removes the {@code Content-length} header, if the message is empty or
	 * null.
	 */
	private void removeContentLengt() {
		headers.remove("Content-length");
	}

	/**
	 * Get the requests command.
	 * 
	 * @return the command of the message
	 */
	public SACommand getCommand() {
		return command;
	}

	/**
	 * Set the user of the SpamAssassin request.
	 * 
	 * @param user
	 *            Username of the user on whose behalf this scan is being performed
	 */
	public void setUser(String user) {
		headers.put("User", user);
	}

	/**
	 * Get the username.
	 * 
	 * @return Username of the user on whose behalf this scan is being performed
	 */
	public String getUser() {
		return headers.containsKey("User") ? (String) headers.get("User") : null;
	}

	/**
	 * Set the command of the request, which is described in {@link SACommand}.
	 * 
	 * @param command
	 *            which command we want to perform with the request
	 */
	public void setCommand(SACommand command) {
		this.command = command;
	}

	/**
	 * Get the SpamAssassin request message (email).
	 * 
	 * @return the message of the request
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set a new message (email) for the SpamAssassin request. It set the
	 * {@code Content-length} header with the length of the message.
	 * 
	 * @param message
	 *            which we want to send for SpamAssassin
	 */
	public void setMessage(String message) {
		this.message = message;
		if (message != null) {
			this.containMessage = true;
			setContentLength(message.length());
		} else {
			removeContentLengt();
			this.containMessage = false;
		}
	}

	/**
	 * Check the SpamAssassin request for it has a message or not.
	 * 
	 * @return true if it has a message, else false
	 */
	public boolean hasMessage() {
		return containMessage;
	}

	/**
	 * Compress the message with {@link Zlib} protocol or not.
	 * 
	 * @param useCompression
	 *            compress it or not
	 */
	public void useCompression(boolean useCompression) {
		this.useCompression = useCompression;
	}

	/**
	 * The message compressed with {@link Zlib} or not.
	 * 
	 * @return compressed or not
	 */
	public boolean isUseCompression() {
		return this.useCompression;
	}

	/**
	 * Get the request headers as a string as the SpamAssassin protocol want.
	 * 
	 * @return the request headers
	 */
	protected String getHeaders() {
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, Object> entry : headers.entrySet()) {
			buffer.append(entry.getKey()).append(": ").append(entry.getValue()).append(NEW_LINE);
		}
		return buffer.toString();
	}

	/**
	 * Convert the request to SpamAssassin protocol compatible and return it as a
	 * {@code ByteArrayOutputStream}.
	 * 
	 * @return a {@code ByteArrayOutputStream} which contain the request itself
	 * @throws IOException
	 *             if an I/O error occurs or the UTF-8 charset is not supported
	 */
	public ByteArrayOutputStream getRequestByteArray() throws IOException {
		String requestProtocol = getCommand().name() + ' ' + "SPAMC/" + PROTOCOL_VERSION + NEW_LINE;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(requestProtocol.getBytes(ENCODING));
		if (useCompression) {
			headers.put("Compress", "zlib");
		}
		byte[] byteEmail = useCompression ? Zlib.compress(message.getBytes(ENCODING)) : message.getBytes(ENCODING);
		setContentLength(byteEmail.length);
		outputStream.write(getHeaders().getBytes(ENCODING));
		outputStream.write(NEW_LINE.getBytes(ENCODING));
		outputStream.write(byteEmail);
		return outputStream;

	}

}
