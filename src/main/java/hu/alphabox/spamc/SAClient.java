package hu.alphabox.spamc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SAClient {

	private final InetAddress address;
	private final int port;

	public SAClient(InetAddress address) {
		this(address, 783);
	}

	public SAClient(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public SAResponse sendRequest(SARequest request) throws SAException, IOException {
		try (Socket socket = new Socket(address, port)) {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();

			StringBuilder builder = new StringBuilder();
			outputStream.write(request.getRequestByteArray().toByteArray());
			outputStream.flush();

			byte[] cache = new byte[1024];
			while (inputStream.read(cache) != -1) {
				builder.append(new String(cache));
			}
			return new SAResponse(builder.toString());

		}
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

}
