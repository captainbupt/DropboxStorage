package Compress;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	/**
	 * Converts an array of bytes into a readable set of characters in the range
	 * ! through ~
	 * 
	 * @param bytes
	 *            The array of bytes
	 * @return A string with characters in the range ! through ~
	 */
	private static String makeReadable(byte[] bytes) {
		for (int ii = 0; ii < bytes.length; ii++) {
			bytes[ii] = (byte) ((bytes[ii] & 0x5E) + 32); // Convert to
															// character !
															// through ~
		}
		return new String(bytes);
	}

	/**
	 * produce a hash of a given string
	 * 
	 * @param str
	 *            The string to hash
	 * @return Returns a collection of sixteen "readable" characters (! through
	 *         ~) corresponding to this string.
	 */
	public static String MD5Cal(String str) {
		// setup the digest
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Hash digest format not known!");
			System.exit(-1);
		}
		return makeReadable(md.digest());
	}
}
