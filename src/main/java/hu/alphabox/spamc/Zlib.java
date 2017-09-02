package hu.alphabox.spamc;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zlib {

	public static byte[] compress(byte[] data) {
		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		deflater.setInput(data);
		deflater.finish();
		byte[] buffer = new byte[1024];
		while(!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		deflater.end();
		outputStream.write('\0');
		return outputStream.toByteArray();
	}
	
	public static byte[] uncompress(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			while(!inflater.finished()) {
					int count = inflater.inflate(buffer);
					outputStream.write(buffer, 0, count);
			}
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
}
