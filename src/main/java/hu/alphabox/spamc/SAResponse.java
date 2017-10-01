package hu.alphabox.spamc;

import java.util.Locale;
import java.util.regex.Pattern;

public class SAResponse {

	private static final Pattern SPLIT_PATTERN;
	private static final Pattern DSPLIT_PATTERN;

	static {
		SPLIT_PATTERN = Pattern.compile("\r?\n");
		DSPLIT_PATTERN = Pattern.compile("\r\n\r\n");
	}

	private String message;
	private String headers;

	private int responseLength;
	private String protocolVersion;
	private boolean isSpam;
	private double spamScore;
	private double spamThreshold;

	public SAResponse() {
	}

	public SAResponse(String response) throws SAException {
		String[] splittedResponse = DSPLIT_PATTERN.split(response);
		if (splittedResponse.length > 0) {
			this.headers = splittedResponse[0].replaceAll("\0+", "");
			this.message = splittedResponse.length == 2 ? splittedResponse[1].trim() : "";
		}

		processHeaders(this.headers);
	}

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResponseLength() {
		return responseLength;
	}

	public void setResponseLength(int responseLength) {
		this.responseLength = responseLength;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public boolean isSpam() {
		return isSpam;
	}

	public void setSpam(boolean isSpam) {
		this.isSpam = isSpam;
	}

	public double getSpamScore() {
		return spamScore;
	}

	public void setSpamScore(double spamScore) {
		this.spamScore = spamScore;
	}

	public double getSpamThreshold() {
		return spamThreshold;
	}

	public void setSpamThreshold(double spamThreshold) {
		this.spamThreshold = spamThreshold;
	}

	@Override
	public String toString() {
		return "SAResponse [message=" + message + ", headers=" + headers + ", responseLength=" + responseLength
				+ ", protocolVersion=" + protocolVersion + ", isSpam=" + isSpam + ", spamScore=" + spamScore
				+ ", spamThreshold=" + spamThreshold + "]";
	}

}
