package com.froad.test.order;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

public class HttpPostTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String str = "7xnkh4.com1.z0.glb.clouddn.com";
		String filePath = "E:\\aaa.txt";
		String fileName = "aaa";
		String boundary = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
		try {
			
			File file = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(file);
			
			URL url = new URL(str);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.addRequestProperty("FileName", fileName);
			connection.setRequestProperty("Host", "upload.qiniu.com");
			connection.setRequestProperty("content-type", "multipart/form-data");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
			connection.setRequestProperty("Content-Length",""+fileInputStream.available());
			connection.setRequestProperty("Content-Disposition","form-data; name="+fileName);
			
			MagicMatch match = Magic.getMagicMatch(file, false, true);
            String contentType = match.getMimeType();
            connection.setRequestProperty("Content-Type", contentType);
			
			
			BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());

			// 读取文件上传到服务器
			byte[] bytes = new byte[1024];
			int numReadByte = 0;
			while ((numReadByte = fileInputStream.read(bytes, 0, 1024)) > 0) {
				out.write(bytes, 0, numReadByte);
			}

			out.flush();
			fileInputStream.close();
			
			// 读取URLConnection的响应
			String response = "";
			StringBuffer strBuf = new StringBuffer();  
			DataInputStream reader = new DataInputStream(connection.getInputStream());
			String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
            response = strBuf.toString();
            reader.close();  
            reader = null;  
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
