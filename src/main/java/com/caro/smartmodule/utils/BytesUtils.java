package com.caro.smartmodule.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class BytesUtils {
	private static final String qppHexStr = "0123456789ABCDEF";
	
	/**
	 * Hex char convert to byte array
	 *
	 * @version 1.0
	 * @createTime 2014-3-21,PM2:16:38
	 * @updateTime 2014-3-21,PM2:16:38
	 * @createAuthor Qn Sw team
	 * @updateAuthor Qn Sw team
	 * @updateInfo 
	 *
	 * @param hexString
	 * @return
	 */
	 public static byte[] hexStr2Bytes(String hexString){
		 if(hexString == null || hexString.isEmpty())
		 {
			 return null;
		 }
		 
		 hexString = hexString.toUpperCase();
		 
		 int length = hexString.length() >> 1;
		 char[] hexChars = hexString.toCharArray();
		 
		 int i = 0;
		 Log.i("QnDbg","hexString.length() : "+hexString.length());
		 
		do{
			 int checkChar = qppHexStr.indexOf(hexChars[i]);
			 
			 if(checkChar == -1)
				 return null;
			 i++;
		 }while (i < hexString.length());
		  
		 byte[] dataArr = new byte[length];
		 
		 
		 for(i = 0; i < length; i++)
		 {
			 int strPos = i*2;
			 
			 dataArr[i] = (byte)(charToByte(hexChars[strPos]) << 4 | charToByte(hexChars[strPos+1]));
		 }
		 
		 return dataArr;
	 }
	 
	 private static byte charToByte(char c)
	 {
		 return (byte)qppHexStr.indexOf(c);
	 }

	/**
	 * byte[] 转为16进制HexString
	 */
	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret.trim();
	}

	/**
	 * 十六进制转换字符串
	 * @param hexStr str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static String hexStr2Str(String hexStr)
	{
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++)
		{
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}


	public static String getString(ByteBuffer buffer) {
		Charset charset = null;
		CharsetDecoder decoder = null;
		CharBuffer charBuffer = null;
		try {
			charset = Charset.forName("UTF-8");
			decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "error";
		}
	}

	/**
	 * 从一个byte[]数组中截取一部分
	 *
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++) bs[i - begin] = src[i];
		return bs;
	}

	public static byte[] ObjectToByte(Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * int到byte[]
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		//由高位到低位
		result[0] = (byte)((i >> 24) & 0xFF);
		result[1] = (byte)((i >> 16) & 0xFF);
		result[2] = (byte)((i >> 8) & 0xFF);
		result[3] = (byte)(i & 0xFF);
		return result;
	}


	/**
	 * 把字节数组转换成16进制字符串
	 *
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}



	/**
	 * convert byte array to integer type value
	 * possible returned value: byte/short/int/long
	 * Note: max support long -> input byte array length should not exceed 8
	 * <p>
	 * byte 转 int （long）
	 * etc:
	 * byte[] bytes = new byte[]{0, 0, 0, -35};
	 * long x = byteArrToInteger(bytes);
	 *
	 * @param byteArr
	 * @return
	 */
	public static long byteArrToInteger(byte[] byteArr) {
		long convertedInterger = 0;
		for (int i = 0; i < byteArr.length; i++) {
			byte curValue = byteArr[i];
			long shiftedValue = curValue << (i * 8);
			long mask = 0xFF << (i * 8);
			long maskedShiftedValue = shiftedValue & mask;
			convertedInterger |= maskedShiftedValue;
		}

		return convertedInterger;
	}

	/**
	 * int 转 byte
	 *
	 * @param inputIntergerValue
	 * @param byteLen
	 * @return
	 */
	public static byte[] integerToByteArr(long inputIntergerValue, int byteLen) {
		byte[] convertedByteArr = new byte[byteLen];
		for (int i = 0; i < convertedByteArr.length; i++) {
			convertedByteArr[i] = (byte) ((inputIntergerValue >> (8 * i)) & 0xFF);
		}

		return convertedByteArr;
	}

	/**
	 * byte 转成 String
	 * @param byteArray
	 * @return
	 */
	public static String byteArrayToStr(byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}
		String str = new String(byteArray);
		return str;
	}

}

