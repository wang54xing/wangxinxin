package com.sys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import com.sys.constants.Constant;

public class MD5Utils {

	private static final Logger logger =Logger.getLogger(MD5Utils.class);

	/**
	 * 默认的密码字符串组合，apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	private static int count = 4;
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance(Constant.MD5);
		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
		}
	}

	/**
	 * 向getMD5方法传入一个你需要转换的原始字符串，将返回字符串的MD5码
	 * 
	 * @param code 原始字符串
	 * @return 返回字符串的MD5码
	 */
	public static String getMD5(String code) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(Constant.MD5);
		String s = code;
		for (int i = 0; i < count; i++) {
			s = getMD5Helper(s, messageDigest);
		}

		return s;
	}

	private static String getMD5Helper(String code, MessageDigest messageDigest) throws Exception {
		byte[] bytes = code.getBytes();
		byte[] results = messageDigest.digest(bytes);
		StringBuilder stringBuilder = new StringBuilder();
		for (byte result : results) {
			// 将byte数组转化为16进制字符存入stringbuilder中
			stringBuilder.append(String.format("%02x", result));
		}

		return stringBuilder.toString();
	}

	/**
	 * 适用于上G大的文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password);
		return s.equals(md5PwdStr);
	}

	public static String getMD5String(String s) {
		String string = s;
		for (int i = 0; i < count; i++) {
			string = getMD5String(string.getBytes());
		}
		return string;
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	public static String md5(String s)
	{
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
