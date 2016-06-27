package com.froad.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;

import com.froad.enums.ResultCode;
import com.froad.exceptions.FroadBusinessException;
import com.froad.logback.LogCvt;
import com.froad.util.Checker;
import com.froad.util.PropertiesUtil;
 
/**
 * Java自带的API对FTP的操作
 * @Title:Ftp.java
 * @author: 张开 
 */
public class FtpUtil {
	
	/**
	 * 配置文件Map
	 */
	public static final Map<String, String> SFTP_PROPERTIES = PropertiesUtil.loadProperties(FtpConstants.FTP_PROPERTIES_NAME);
	
    /**
     * 本地文件名
     */
    private String localFileName;
    /**
     * 远程文件名
     */
    private String remoteFileName;
    /**
     * FTP客户端
     */
    private FtpClient ftpClient;
    
    /**
     * 服务器连接
     * @param ip 服务器IP
     * @param port 服务器端口
     * @param user 用户名
     * @param password 密码
     * @param path 服务器路径
     * @author 张开
     * @throws FroadBusinessException 
     * @date  2015-08-26
     */
	public void connectServer() throws FroadBusinessException {

		long startTime = System.currentTimeMillis();

		String host = SFTP_PROPERTIES.get(FtpConstants.FTP_HOST);
		String port = SFTP_PROPERTIES.get(FtpConstants.FTP_PORT);
		String userName = SFTP_PROPERTIES.get(FtpConstants.FTP_USERNAME);
		String password = SFTP_PROPERTIES.get(FtpConstants.FTP_PASSWORD);
		String remoteFileDir = SFTP_PROPERTIES.get(FtpConstants.REMOTE_FILE_DIR);

		try {
			/*******连接服务器的两种方法*******/
			// 第一种方法
			 /*ftpClient = new FtpClient();
			 ftpClient.openServer(host, 22);*/
			
			// 第二种方法
			ftpClient = new FtpClient(host);
			ftpClient.login(userName, password);
			// 设置成2进制传输
			ftpClient.binary();
			if (StringUtils.isNotEmpty(remoteFileDir)) {
				// 把远程系统上的目录切换到参数path所指定的目录
				try {
					ftpClient.cd(remoteFileDir);
				} catch (IOException e) {
					ftpClient.sendServer("XMKD " + remoteFileDir + "\r\n");
					ftpClient.readServerResponse();
				}
			}
			ftpClient.binary();
		} catch (IOException e) {
			LogCvt.error("上传失败，FTP远程连接失败：",e);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"FTP连接失败");
		}
		long endTime = System.currentTimeMillis();
		LogCvt.info("FTP "+host+" 连接成功！耗时：" + (endTime - startTime) + "ms");
	}
 
    /**
     * 服务器连接
     * @param ip 服务器IP
     * @param port 服务器端口
     * @param user 用户名
     * @param password 密码
     * @param path 服务器路径
     * @author 张开
     * @date   2015-08-26
     */
    public void connectServer(String ip, int port, String user, String password, String path) {
        try {
        	/*******连接服务器的两种方法*******/
        	
            //第一种方法
            /*ftpClient = new FtpClient();
            ftpClient.openServer(ip, port);*/
        	
            //第二种方法
            ftpClient = new FtpClient(ip);
            ftpClient.login(user, password);
            // 设置成2进制传输
            ftpClient.binary();
            System.out.println("login success!");
            if (path.length() != 0){
                //把远程系统上的目录切换到参数path所指定的目录
                ftpClient.cd(path);
            }
            ftpClient.binary();
        } catch (IOException ex) {
            LogCvt.error("FTP连接异常：", ex);
            throw new RuntimeException(ex);
        }
    }
    /**
     * 关闭连接
     * @author 张开
     * @date   2015-08-26
     */
    public void closeConnect() {
        try {
            ftpClient.closeServer();
            LogCvt.info("关闭FTP连接成功！");
        } catch (IOException ex) {
        	LogCvt.error("关闭FTP连接异常：", ex);
        }
    }
    
    /**
     * 上传文件
     * @param localFile 本地文件
     * @param remoteFile 远程文件
     * @author 张开
     * @throws FroadBusinessException 
     * @date   2015-08-26
     */
	private String uploadFile(String fileName) throws FroadBusinessException {
		return uploadFile(fileName,false,null);
	}
	
	/**
     * 上传文件
     * @param localFile 本地文件
     * @param remoteFile 远程文件
     * @author 张开
     * @throws FroadBusinessException 
     * @date   2015-08-26
     */
	private String uploadFile(String fileName,boolean isLocalFileInFolder,String localFileFolder) throws FroadBusinessException {
		long startTime = System.currentTimeMillis();
		String localFileDir = SFTP_PROPERTIES.get(FtpConstants.LOCAL_FILE_DIR);
		String remoteFileDir = SFTP_PROPERTIES.get(FtpConstants.REMOTE_FILE_DIR) + getSubFileDir();
		String nginxRootDir = SFTP_PROPERTIES.get(FtpConstants.NGINX_ROOT_DIR);
		String backslash = FtpConstants.SLASH;
		if (isWindowsOs()) {
			backslash = FtpConstants.DOUBLE_BACKSLASH;
		}
		if(isLocalFileInFolder){
			if(localFileFolder == null || localFileFolder.trim() == ""){
				localFileFolder = "";
			}
			this.localFileName = localFileDir + backslash + localFileFolder + backslash + fileName;
		}else{
			this.localFileName = localFileDir + backslash + fileName;
		}
		
		this.remoteFileName = remoteFileDir + FtpConstants.SLASH +fileName;

		try {
			ftpClient.cd(remoteFileDir);
		} catch (IOException e) {
			ftpClient.sendServer("XMKD " + remoteFileDir + "\r\n");
			try {
				ftpClient.readServerResponse();
			} catch (IOException ex) {
				LogCvt.error("上传失败，创建文件目录失败：" , ex);
				throw new FroadBusinessException(ResultCode.failed.getCode(),"上传失败");
			}
		}

		TelnetOutputStream os = null;
		FileInputStream is = null;
		int fileSize = 0;
		try {
			// 将远程文件加入输出流中
			os = ftpClient.put(this.remoteFileName);
			// 获取本地文件的输入流
			File file_in = new File(this.localFileName);
			is = new FileInputStream(file_in);
			fileSize = is.available();
			// 创建一个缓冲区
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
		} catch (IOException e) {
			LogCvt.error("上传失败！" , e);
			throw new FroadBusinessException(ResultCode.failed.getCode(),"上传失败");
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				LogCvt.error("关闭FileInputStream失败！" , e);
			} finally {
				try {
					if (os != null) {
						os.close();
					}
				} catch (IOException e) {
					LogCvt.error("关闭TelnetOutputStream失败！" , e);
				}
			}
		}
		long endTime = System.currentTimeMillis();
		String url = this.remoteFileName.replace(nginxRootDir, "");
		LogCvt.info("[文件：" + fileName + " (" + FormetFileSize(fileSize) + ")]上传成功！返回URL：" + url + "，上传耗时：" + (endTime - startTime) + "ms");
		return url;
	}
	
	private String getSubFileDir() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}
	
	private boolean isWindowsOs(){
		return System.getProperty("os.name").contains("windows") || System.getProperty("os.name").contains("Windows");
	}
	
	private String FormetFileSize(long fileSize) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}
    
    /**
     * 上传文件
     * @param localFile 本地文件
     * @param remoteFile 远程文件
     * @author 张开
     * @date   2015-08-26
     */
	private void uploadFile(String localFile, String remoteFile) {
		long startTime = System.currentTimeMillis();
		this.localFileName = localFile;
		this.remoteFileName = remoteFile;
		TelnetOutputStream os = null;
		FileInputStream is = null;
		try {
			// 将远程文件加入输出流中
			os = ftpClient.put(this.remoteFileName);
			// 获取本地文件的输入流
			File file_in = new File(this.localFileName);
			is = new FileInputStream(file_in);
			// 创建一个缓冲区
			byte[] bytes = new byte[1024];
			int c;
			while ((c = is.read(bytes)) != -1) {
				os.write(bytes, 0, c);
			}
			System.out.println("upload success");
		} catch (IOException ex) {
			System.out.println("not upload");
			ex.printStackTrace();
			throw new RuntimeException(ex);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (os != null) {
						os.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		long endTime = System.currentTimeMillis();
		LogCvt.info("上传耗时：" + (endTime - startTime) + "ms");
	}
     
    /**
     * 下载文件
     * @param remoteFile 远程文件路径(服务器端)
     * @param localFile 本地文件路径(客户端)
     * @author 张开
     * @date   2015-08-26
     */
    public void download(String remoteFile, String localFile) {
        TelnetInputStream is = null;
        FileOutputStream os = null;
        try {
            //获取远程机器上的文件filename，借助TelnetInputStream把该文件传送到本地。
            is = ftpClient.get(remoteFile);
            File file_in = new File(localFile);
            os = new FileOutputStream(file_in);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {
                os.write(bytes, 0, c);
            }
            System.out.println("download success");
        } catch (IOException ex) {
            System.out.println("not download");
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally{
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
            	LogCvt.error("关闭TelnetInputStream异常:", e);
            } finally {
                try {
                    if(os != null){
                        os.close();
                    }
                } catch (IOException e) {
                	LogCvt.error("关闭FileOutputStream异常:", e);
                }
            }
        }
    }
    
    public static String upload(String fileName)throws FroadBusinessException {
		String url = "";
		FtpUtil ftp = new FtpUtil();
		// 使用配置的端口号、用户名、密码以及根目录连接FTP服务器
		ftp.connectServer();
		// 上传
		url = ftp.uploadFile(fileName);
		ftp.closeConnect();
		return url;
	}
    
	public static String upload(String fileName, String folder)throws FroadBusinessException {
		String url = "";
		FtpUtil ftp = new FtpUtil();
		// 使用配置的端口号、用户名、密码以及根目录连接FTP服务器
		ftp.connectServer();
		// 上传
		boolean isFileInFolder = false;
		if (Checker.isNotEmpty(folder)) {
			isFileInFolder = true;
		}
		url = ftp.uploadFile(fileName, isFileInFolder, folder);
		ftp.closeConnect();
		return url;
	}
    
}
