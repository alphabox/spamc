package hu.alphabox.spamc;

public class SAException extends Exception {

	private static final long serialVersionUID = 8772979000813860774L;

	private final int errorCode;

	public SAException(String message) {
		this(message, -1);
	}

	public SAException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
