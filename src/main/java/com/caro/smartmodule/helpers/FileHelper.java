package com.caro.smartmodule.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * class name：FileService<BR>
 * class description：android文件的一些读取操作<BR>
 * PS： <BR>
 * 
 * @version 1.00 2010/10/21
 * @author CODYY)peijiangping
 */
public class FileHelper {
	private Context context;

	public FileHelper(Context c) {
		this.context = c;
	}


	// 往sd卡中写入文件
	public void writeSDCardFile(String path, byte[] buffer) throws IOException {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(buffer);// 写入buffer数组。如果想写入一些简单的字符，可以将String.getBytes()再写入文件;
		fos.close();
	}

	// 将文件写入应用的data/data的files目录下
	public void writeDateFile(String fileName, byte[] buffer) throws Exception {
		byte[] buf = fileName.getBytes("iso8859-1");
		fileName = new String(buf, "utf-8");
		// Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
		// Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
		// Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
		// MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
		// 如果希望文件被其他应用读和写，可以传入：
		// openFileOutput("output.txt", Context.MODE_WORLD_READABLE +
		// Context.MODE_WORLD_WRITEABLE);
		FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);// 添加在文件后面
		fos.write(buffer);
		fos.close();
	}


}
