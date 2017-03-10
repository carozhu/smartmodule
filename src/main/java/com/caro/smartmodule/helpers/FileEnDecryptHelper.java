package com.caro.smartmodule.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 加密解密管理类
 * 
 * 加密算法 : 将文件的数据流的每个字节与该字节的下标异或. 解密算法 : 已经加密的文件再执行一次对文件的数据流的每个字节与该字节的下标异或
 * 
 * @author Administrator
 * 
 */
public class FileEnDecryptHelper {
	/**
	 * 记录上次解密过的文件名
	 */
	private  String LastDecryptFile;

	private FileEnDecryptHelper(String LastDecryptFile) {
		this.LastDecryptFile=LastDecryptFile;
	}

	private static FileEnDecryptHelper instance = null;

	public static FileEnDecryptHelper getInstance(String pathFile) {
		synchronized (FileEnDecryptHelper.class) {
			if (instance == null)
				instance = new FileEnDecryptHelper(pathFile);
		}
		return instance;
	}



	/**
	 * LastDecryptFilename.ttt 文件是否被清空
	 */
	private boolean isClear = false;

	/**
	 * 加密入口
	 * 
	 * @param fileUrl
	 *            文件绝对路径
	 * @return
	 */
	public boolean InitEncrypt(String fileUrl) {
		encrypt(fileUrl);
		return true;
	}

	private final int REVERSE_LENGTH = 56;

	/**
	 * 加密
	 * 
	 * @param strFile
	 *            源文件绝对路径
	 * @return
	 */
	private boolean encrypt(String strFile) {
		int len = REVERSE_LENGTH;
		try {
			File f = new File(strFile);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			long totalLen = raf.length();

			if (totalLen < REVERSE_LENGTH)
				len = (int) totalLen;

			FileChannel channel = raf.getChannel();
			MappedByteBuffer buffer = channel.map(
					FileChannel.MapMode.READ_WRITE, 0, REVERSE_LENGTH);
			byte tmp;
			for (int i = 0; i < len; ++i) {
				byte rawByte = buffer.get(i);
				tmp = (byte) (rawByte ^ i);
				buffer.put(i, tmp);
			}
			buffer.force();
			buffer.clear();
			channel.close();
			raf.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 解密入口
	 * 
	 * @param fileUrl
	 *            源文件绝对路径
	 */
	public void Initdecrypt(String fileUrl) {
		try {
			if (isDecripted(fileUrl)) {
				decrypt(fileUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decrypt(String fileUrl) {
		encrypt(fileUrl);
	}

	/**
	 * fileName 文件是否已经解密了
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private boolean isDecripted(String fileName) throws IOException {
		// 上次加密的文件
		File lastDecryptFile = new File(LastDecryptFile);
		if (lastDecryptFile.exists() && isClear == false) {
			String lastDecryptfilepath = getLastDecryptFilePath(LastDecryptFile);
			if (lastDecryptfilepath != null
					&& lastDecryptfilepath.equals(fileName)) {
				return false;
			} else {
				clear();
			}
		}
		StringBufferWrite(fileName);
		return true;
	}

	/**
	 * 将需要加密的文件绝对路径写入LastDecryptFile
	 * 
	 * @param filePath
	 *            需要加密的文件绝对路径
	 * @param filePath
	 * @throws IOException
	 */
	private void StringBufferWrite(String filePath) throws IOException {
		File lastDecryptFile = new File(LastDecryptFile);
		if (!lastDecryptFile.exists())
			lastDecryptFile.createNewFile();
		FileOutputStream out = new FileOutputStream(lastDecryptFile, true);
		StringBuffer sb = new StringBuffer();
		sb.append(filePath);
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
	}

	/**
	 * 清空加密记录
	 */
	public synchronized void clear() {
		isClear = true;
		File decryptTempFile = new File(LastDecryptFile);
		if (decryptTempFile.exists()) {
			try {
				String fileName = getLastDecryptFilePath(LastDecryptFile);
				decrypt(fileName);
				new File(LastDecryptFile).delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		isClear = false;
	}

	/**
	 * 从LastDecryptFile中读取记录
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private String getLastDecryptFilePath(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String str = br.readLine();
		br.close();
		return str;
	}
}
