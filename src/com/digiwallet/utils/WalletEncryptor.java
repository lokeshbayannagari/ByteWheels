package com.digiwallet.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author lokesh
 */
public class WalletEncryptor
{
    WalletLogger logger = new WalletLogger(WalletEncryptor.class);

    public WalletEncryptor()
    {
    }

    public String getEncryptedString(String strToBeEncrypted, String algorithm)
    {
	String METHOD_NAME = "getSHA1Encrypted";
	logger.logEntering(METHOD_NAME);
	String strReturn = null;
	try
	{
	    MessageDigest md = MessageDigest.getInstance(algorithm);
	    md.reset();
	    md.update(strToBeEncrypted.getBytes(WalletConstants.ENCODING_STANDARD));
	    BigInteger hash = new BigInteger(1, md.digest());
	    // convert to hexadecimal string
	    strReturn = hash.toString(16);
	    //40 should be the length of SHA-1 hash
	    while(strReturn.length() < 40)
	    {
		strReturn = "0" + strReturn;
	    }
	}
	catch(NoSuchAlgorithmException | UnsupportedEncodingException | NullPointerException ex)
	{
	    logger.logError(METHOD_NAME, ex);
	}
	logger.logExiting(METHOD_NAME, strReturn);
	return strReturn;
    }
}
