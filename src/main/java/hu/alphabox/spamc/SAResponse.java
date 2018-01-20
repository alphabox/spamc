package hu.alphabox.spamc;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Store the parsed response details from SpamAssassin server. The
 * {@code SAResponse} contains details about the message, such as, the message
 * is spam or not, the spam score/threshold, and the modified headers and
 * message (if we choose the right {@code SACommand}}.
 * 
 * The response also contains the SpamAssassin protocol version.
 * 
 * @author Daniel Mecsei
 *
 */
public class SAResponse {

	/**
	 * A simple pattern to split response message around new lines.
	 */
	private static final Pattern SPLIT_PATTERN;

	/**
	 * A simple pattern to split response message around double new lines.
	 */
	private static final Pattern DSPLIT_PATTERN;

	static {
		SPLIT_PATTERN = Pattern.compile("\r?\n");
		DSPLIT_PATTERN = Pattern.compile("\r\n\r\n");
	}

	/**
	 * The modified message or symbols that match to the message.
	 */
	private String message;

	/**
	 * The SpamAssassin responses headers.
	 */
	private String headers;

	/**
	 * The response content length.
	 */
	private int responseLength;

	/**
	 * The SpamAssassin server protocol version.
	 */
	private String protocolVersion;

	/**
	 * The message is spam or not.
	 */
	private boolean isSpam;

	/**
	 * The message spam score from SpamAssassin server.
	 */
	private double spamScore;

	/**
	 * The adjusted threshold of the server and the current message.
	 */
	private double spamThreshold;

	/**
	 * Create a new {@code SAResponse} object with default values.
	 */
	public SAResponse() {
	}

	/**
	 * Create a new {@code SAResponse} object which parse the response from
	 * SpamAssassin server.
	 * 
	 * @param response
	 *            the response message from SA
	 * @throws SAException
	 *             If the response message cannot parsed or contains a header that
	 *             we cannot recognize.
	 */
	public SAResponse(String response) throws SAException {
		String[] splittedResponse = DSPLIT_PATTERN.split(response);
		if (splittedResponse.length > 0) {
			this.headers = splittedResponse[0].replaceAll("\0+", "");
			this.message = splittedResponse.length == 2 ? splittedResponse[1].trim() : "";
		}

		processHeaders(this.headers);
	}

	/**
	 * Parse the SpamAssassin server response messages headers.
	 * 
	 * @param headers
	 *            the SA reponse raw headers
	 * @throws SAException
	 *             If we cannot parse the raw headers or contains a header that we
	 *             cannot recognize.
	 */
	private void processHeaders(String headers) throws SAException {
		String[] lines = SPLIT_PATTERN.split(headers);
		for (int i = 0; i < lines.length; i++) {
			String[] lineSplit = null;
			String s[] = lines[i].split("[/:\\s]", 2);
			switch (s[0].toLowerCase(Locale.ROOT)) {
			// Empty line as response to SKIP command
			case "":
				break;
			case "spamd":
				lineSplit = lines[i].split("[ /]");
				int errorCode = Integer.parseInt(lineSplit[2]);
				if (errorCode != 0) {
					throw new SAException(lines[i], errorCode);
				}
				this.protocolVersion = lineSplit[1];
				break;
			case "spam":
				lineSplit = lines[i].split("[:;/]");
				this.isSpam = lineSplit[1].trim().equalsIgnoreCase("true") ? true : false;
				this.spamScore = Double.parseDouble(lineSplit[2].trim());
				this.spamThreshold = Double.parseDouble(lineSplit[3].trim());
				break;
			case "content-length":
				lineSplit = lines[i].split(": ");
				this.responseLength = Integer.parseInt(lineSplit[1]);
				break;
			case "didset":
			case "didremove":
				// TODO: Save variable
				lineSplit = lines[i].split(": ");
				break;
			default:
				throw new SAException("Unrecognized header line: " + lines[i]);
			}
		}
	}

	/**
	 * Contains the modified message, the report from message, or the symbols that match to the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * The parsed headers of the SpamAssassin response.
	 * @return the headers
	 */
	public String getHeaders() {
		return headers;
	}

	/**
	 * Returns with the SpamAssassin response length.
	 * @return the responseLength
	 */
	public int getResponseLength() {
		return responseLength;
	}

	/**
	 * Returns with the SpamAssassin server protocol version.
	 * @return the protocolVersion
	 */
	public String getProtocolVersion() {
		return protocolVersion;
	}

	/**
	 * Return true if the message is spam, else false.
	 * @return return the isSpam
	 */
	public boolean isSpam() {
		return isSpam;
	}

	/**
	 * The spam score of the message.
	 * @return the spamScore
	 */
	public double getSpamScore() {
		return spamScore;
	}

	/**
	 * The spam threshold of the server.
	 * @return the spamThreshold
	 */
	public double getSpamThreshold() {
		return spamThreshold;
	}

	@Override
	public String toString() {
		return "SAResponse [message=" + message + ", headers=" + headers + ", responseLength=" + responseLength
				+ ", protocolVersion=" + protocolVersion + ", isSpam=" + isSpam + ", spamScore=" + spamScore
				+ ", spamThreshold=" + spamThreshold + "]";
	}

}
