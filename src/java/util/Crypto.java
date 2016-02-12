package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto 
{

	/** La fonction de MD5
	 * @param input
	 * @return
	 */
	public static String encrypt(String input) 
	{
		String hash = null;
		if(null == input) return null;
		try 
		{
			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			//Converts message digest value in base 16 (hex) 
			hash = new BigInteger(1, digest.digest()).toString(16);
		}
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return hash;
	}
}
