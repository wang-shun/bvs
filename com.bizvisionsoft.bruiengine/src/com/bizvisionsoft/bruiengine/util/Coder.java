package com.bizvisionsoft.bruiengine.util;

import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public abstract class Coder {
	public static final String KEY_SHA = "SHA"; //$NON-NLS-1$
	public static final String KEY_MD5 = "MD5"; //$NON-NLS-1$

	/**
	 * MAC�㷨��ѡ���¶����㷨
	 * 
	 * <pre>
	 * HmacMD5  
	 * HmacSHA1  
	 * HmacSHA256  
	 * HmacSHA384  
	 * HmacSHA512
	 * </pre>
	 */
	public static final String KEY_MAC = "HmacMD5"; //$NON-NLS-1$

	/**
	 * BASE64����
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return new Base64().decode(key);
	}

	/**
	 * BASE64����
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.encodeBase64String(key);
	}

	/**
	 * MD5����
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);

		return md5.digest();

	}
	

	/**
	 * SHA����
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] data) throws Exception {

		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);

		return sha.digest();

	}

	/**
	 * ��ʼ��HMAC��Կ
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);

		SecretKey secretKey = keyGenerator.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC����
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptHMAC(byte[] data, String key) throws Exception {

		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);

		return mac.doFinal(data);

	}
	
	public static void main(String[] args) throws Exception {
		String psw;

		System.out.println("\n[BASE 64���ܽ���]");
		psw = "abc.����֧��";
		System.out.println("ԭ��:"+psw);
		psw = Coder.encryptBASE64(psw.getBytes());
		System.out.println("����"+psw);
		byte[] res = Coder.decryptBASE64(psw);
		psw = new String(res);
		System.out.println("����"+psw);
		
		System.out.println("\n[����MD5��]");
		psw = "abc.����֧��";
		System.out.println("ԭ��:"+psw);
		byte[] bytes = Coder.encryptMD5(psw.getBytes());
		psw = new String(bytes);
		System.out.println("����"+psw);
		
		System.out.println("\n[RSA���ܽ���]");
		psw = "abc.����֧��";
		System.out.println("ԭ��:"+psw);
		String[] keys = RSACoderUtil.getKeys();
		System.out.println("��Կ:" + keys[0]);
		System.out.println("˽Կ:" + keys[1]);

		byte[] data = psw.getBytes();
		byte[] encrypted = RSACoder.encryptByPublicKey(data, keys[0]);
		System.out.println("����"+new String(encrypted));
		byte[] decrypted = RSACoder.decryptByPrivateKey(encrypted, keys[1]);
		System.out.println("����"+new String(decrypted));

	}
}