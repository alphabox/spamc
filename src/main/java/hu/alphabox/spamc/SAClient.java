package hu.alphabox.spamc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SAClient {

	public static final String PROTOCOL_VERSION = "1.5";

	private final InetAddress address;
	private final int port;

	public SAClient(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}

	public SAResponse sendRequest(SARequest request) throws SAException {
		try (Socket socket = new Socket(address, port)) {
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();

			StringBuilder builder = new StringBuilder();
			builder.append(request.getCommand().name()).append(' ');
			builder.append("SPAMC/").append(PROTOCOL_VERSION).append("\r\n");
			builder.append(request.getRequest());
			System.out.println(builder.toString());
			outputStream.write(builder.toString().getBytes());
			outputStream.flush();

			builder.setLength(0);

			byte[] cache = new byte[1024];

			while (inputStream.read(cache) != -1) {
				builder.append(new String(cache));
			}
			return new SAResponse(builder.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

}
