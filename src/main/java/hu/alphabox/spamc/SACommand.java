package hu.alphabox.spamc;

/**
 * The class contains the available commands for spamd to execute.
 * 
 * @author Mecsei Dániel
 * @see {@link https://svn.apache.org/repos/asf/spamassassin/trunk/spamd/PROTOCOL}
 *
 */
public enum SACommand {

	/**
	 * Check if the passed message is spam or not and reply with a True/False
	 * result, a score and the threshold.
	 */
	CHECK,
	/**
	 * Check if the passed message is spam or not and return with a score and list
	 * of symbols hit.
	 */
	SYMBOLS,
	/**
	 * Check if the passed message is spam or not and return with a True/False
	 * result, a score, a threshold and a report which generated by spamd.
	 */
	REPORT,
	/**
	 * Check if the passed message is spam or not and return with a True/False
	 * result, a score, a threshold and a report which generated by spamd.
	 */
	REPORT_IFSPAM,
	/**
	 * Ignore the current message (not process it).
	 */
	SKIP,
	/**
	 * Chech that the spamd is alive or not.
	 */
	PING,
	/**
	 * Process the current message and return with modified headers and message.
	 */
	PROCESS,
	/**
	 * Tell what type of we are to process and what should be done with that
	 * message. This includes setting or removing a local or a remote database
	 * (learning, reporting, forgetting, revoking).
	 */
	TELL,
	/**
	 * Process the current message and return with modified headers.
	 */
	HEADERS;

}
