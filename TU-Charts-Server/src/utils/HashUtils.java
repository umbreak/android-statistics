package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public enum HashUtils {
	i;
	MessageDigest digest=null;
	private HashUtils(){
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
	}
	public String getHash(String password) {
		digest.reset();
		byte[] data=digest.digest(password.getBytes());
		return bin2hex(data);
	}
	static String bin2hex(byte[] data) {
		return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
	}
}
