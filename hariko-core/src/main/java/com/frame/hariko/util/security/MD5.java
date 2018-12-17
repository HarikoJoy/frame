package com.frame.hariko.util.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


/**
 * @author Hai
 *
 */
public final class MD5 {

	private static Logger log = LoggerFactory.getLogger(MD5.class);

	/**
	 * 将数据加密MD5值(默认编码格式为UTF-8)
	 * @param content 需要MD5的值
	 * @return
	 */
	public final static String encrypt(String content) {
		return encrypt(content, "UTF-8");
	}

	/**
	 * 将数据加密MD5值
	 * @param content 需要MD5的值
	 * @param encoding 内容的编码格式 
	 * @return 返回MD5值
	 */
	public final static String encrypt(String content, String encoding) {
		if (content == null) {
			content = "";
		}
		if (encoding == null || "".equals(encoding)) {
			encoding = "UTF-8";
		}
		byte[] bytes;
		try {
			bytes = content.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return encrypt(bytes);
	}

	public final static String encrypt(byte[] content){
		String md5 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(content);
			byte md5Bytes[] = md.digest();
			md5 = HexUtil.bytesToHexString(md5Bytes);
		} catch (Exception e) {
			log.error("MD5 encry error ;", e);
			throw  new RuntimeException(e);
		}
		return md5;
	}


}
