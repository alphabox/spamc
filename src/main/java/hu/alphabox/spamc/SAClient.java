package hu.alphabox.spamc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * </p>
 * Manage connection to SpamAssassin server, send {@code SARequest} to it and
 * read response for them. We can join to SpamAssassin server with different
 * IP/Domain and port combinations.
 * </p>
 * <p>
 * The client make a new connection for every request, because the SpamAssassin
 * server protocol looks like an HTTP request, and we cannot send more then one
 * email per connection.
 * </p>
 * 
 * @author Daniel Mecsei
 *
 */
public class SAClient {

	/**
	 * The connection (IP) address to SpamAssassin server.
	 */
	private final InetAddress address;

	/**
	 * The connection port to SpamAssassin server.
	 */
	private final int port;

	/**
	 * Construct a new {@code SAClient} object with {@code address} and default port
	 * number (783).
	 * 
	 * @param address
	 *            SpamAssassin server {@code InetAddress} address
	 */
	public SAClient(InetAddress address) {
		this(address, 783);
	}

	/**
	 * Construct a new {@code SAClient} object with {@code host} hostname and
	 * default port number (783).
	 * 
	 * @param host
	 *            the host (domain) address of SpamAssassin server
	 * @throws UnknownHostException
	 *             If the IP address of {@code host} cannot determined.
	 */
	public SAClient(String host) throws UnknownHostException {
		this(InetAddress.getByName(host), 783);
	}

	/**
	 * Construct a new {@code SAClient} object with {@code host} hostname and
	 * {@code port } number.
	 * 
	 * @param host
	 *            the host (domain) address of SpamAssassin server
	 * @param port
	 *            the port number of SpamAssassin server
	 * @throws UnknownHostException
	 *             If the IP address of {@code host} cannot determined.
	 */
	public SAClient(String host, int port) throws UnknownHostException {
		this(InetAddress.getByName(host), port);
	}

	/**
	 * Construct a new {@code SAClient} object with {@code address} and {@code port}
	 * number.
	 * 
	 * @param address
	 *            SpamAssassin server {@code InetAddress} address
	 * @param port
	 *            the port number of SpamAssassin server
	 */
	public SAClient(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	/**
	 * Send a {@code SARequest} to SpamAssassin server, read the response from it
	 * and return with a new {@code SAResponse} object.
	 * 
	 * @param request
	 *            {@code SARequest} with specified {@code SACommand} and message
	 * @return SpamAssassin response {@code SAResponse}
	 * @throws SAException
	 *             If the SpamAssassin response not recognized.
	 * @throws IOException
	 *             If we cannot read from input/output stream, the connection timed
	 *             out or close immedietly, or other I/O exception occur.
	 */
	public SAResponse sendRequest(SARequest request) throws SAException, IOException {
		try (Socket socket = new Socket(address, port)) {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();

			StringBuilder builder = new StringBuilder();
			outputStream.write(request.getRequestByteArray().toByteArray());
			outputStream.flush();

			byte[] cache = new byte[1024];
			while (inputStream.read(cache) != -1) {
				builder.append(new String(cache, SARequest.ENCODING));
			}
			return new SAResponse(builder.toString());

		}
	}

	/**
	 * Get the IP address of SpamAssassin server.
	 * 
	 * @return the SA server {@code InetAddress}
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Get the port number of SpamAssassin server, which we added in the
	 * constructor.
	 * 
	 * @return the SA server port number
	 */
	public int getPort() {
		return port;
	}

}
