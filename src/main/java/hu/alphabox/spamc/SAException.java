package hu.alphabox.spamc;

/**
 * The class represents problems about Spamassassin, which has a client or
 * server side problem. The {@code SAException} contains the message from
 * client/server and an errorCode to check the specific problem on SpamAssassin
 * site, if it has any documentation on it.
 * 
 * @author Daniel Mecsei
 *
 */
public class SAException extends Exception {

	private static final long serialVersionUID = 8772979000813860774L;

	/**
	 * The error code from SpamAssassin client or server.
	 */
	private final int errorCode;

	/**
	 * Constructs a new exception with specified message and error code with -1
	 * value.
	 * 
	 * @param message
	 *            the error message
	 */
	public SAException(String message) {
		this(message, -1);
	}

	/**
	 * Constructs a new exception with specified message and error code.
	 * 
	 * @param message
	 *            the error message
	 * @param errorCode
	 *            the error code
	 */
	public SAException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error code integer of this exception.
	 *
	 * @return the error code integer of this {@code SAException} instance (which
	 *         may be {@code -1}).
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
