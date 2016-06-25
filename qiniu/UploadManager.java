package com.froad.test.order;

import java.io.File;
import java.util.Map;

import com.froad.exceptions.FroadBusinessException;
import com.squareup.okhttp.Response;

public class UploadManager {
	
	/**
	 * 上传数据
	 *
	 * @param data     上传的数据 byte[]、File、filePath
	 * @param key      上传数据保存的文件名
	 * @param token    上传凭证
	 * @param params   自定义参数，如 params.put("x:foo", "foo")
	 * @param mime     指定文件mimetype
	 * @param checkCrc 是否验证crc32
	 * @return
	 * @throws QiniuException
	 */
	public Response put(File data, String key, String token) throws FroadBusinessException{
		return  null;
	}
	
	/**
	 * 上传数据
	 *
	 * @param data     上传的数据 byte[]、File、filePath
	 * @param key      上传数据保存的文件名
	 * @param token    上传凭证
	 * @param params   自定义参数，如 params.put("x:foo", "foo")
	 * @param mime     指定文件mimetype
	 * @param checkCrc 是否验证crc32
	 * @return
	 * @throws QiniuException
	 */
	public Response put(File data, String key, String token, Map<String,String> params,String mime, boolean checkCrc) throws FroadBusinessException{
		return  null;
	}
	
}
