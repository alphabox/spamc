package hu.alphabox.spamc;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * A simple helper class to compress/decompress messages with zlib protocol.
 * 
 * @author Daniel Mecsei
 *
 */
public class Zlib {
	
	private Zlib() {}

	/**
	 * Compress message with {@link Deflater} DEFAULT_COMPRESSION level.
	 * 
	 * @param data
	 *            the message/data to compress
	 * @return compressed message
	 */
	public static byte[] compress(byte[] data) {
		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		deflater.setInput(data);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		deflater.end();
		return outputStream.toByteArray();
	}

	/**
	 * Decompress data.
	 * 
	 * @param data
	 *            zlib compressed message/data
	 * @return decompressed message
	 * @throws DataFormatException
	 *             If the {@code data} is not compressed properly or the data is not
	 *             zlib compressed
	 */
	public static byte[] uncompress(byte[] data) throws DataFormatException {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		return outputStream.toByteArray();
	}

}
