package com.froad.test.order;

import java.io.File;
import java.util.Map;

import com.froad.exceptions.FroadBusinessException;
import com.froad.logback.LogCvt;
import com.squareup.okhttp.Response;


public class QiniuServiceImpl{
	
	UploadManager uploadManager = new UploadManager();
	
	public class MyRet {
	    public long fsize;
	    public String key;
	    public String hash;
	    public int width;
	    public int height;
	}
	
	//上传内存中数据
	public void upload(File data, String key, String upToken){
	  try {
	        Response res = uploadManager.put(data, key, upToken);
	        // log.info(res);
	        // log.info(res.bodyString());
	        // Ret ret = res.jsonToObject(Ret.class);
	        if(res.isSuccessful()){
	            //success
	        }else {
	            //
	        }
	    } catch (FroadBusinessException e) {
	        Response r = e.response;
	        // 请求失败时简单状态信息
	        LogCvt.error(r.toString());
	        try {
	            // 响应的文本信息
	        	LogCvt.error(r.bodyString());
	        } catch (FroadBusinessException e1) {
	            //ignore
	        }
	    }
	}


	public void uploadFile(){
	    try {
	        Response res = uploadManager.put(getFile(), getKey(), getUpToken());
	        // log.info(res);
	        // log.info(res.bodyString());
	        // Ret ret = res.jsonToObject(Ret.class);
	    } catch (QiniuException e) {
	        Response r = e.response;
	        // 请求失败时简单状态信息
	        log.error(r.toString());
	        try {
	            // 响应的文本信息
	            log.error(r.bodyString());
	        } catch (QiniuException e1) {
	            //ignore
	        }
	    }
	}

	

}
