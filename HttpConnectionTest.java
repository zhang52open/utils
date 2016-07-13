/**
	 * @Title: requestHttpsPost
	 * @Description:网络处理
	 * @param jsonString
	 * @return
	 * @throws Exception
	 * @author Jiawei.Li
	 */
	private static synchronized String requestHttpsPost(String jsonString)
			throws Exception {
		String path = MPConfig.CODE_ON_PAY_URL;
		HttpsURLConnection httpsConn = null;
		InputStream inStream = null;
		String responseData = null;
		LogFactory.e(TAG,"  requestHttpsPost jsonString  "+jsonString);
		byte[] byteData = jsonString.getBytes();
		URL url = new URL(path);
		if (url.getProtocol().toLowerCase().equals("https")) {
			HttpsUtils.trustAllHosts();
			httpsConn = (HttpsURLConnection) url.openConnection();
			httpsConn.setHostnameVerifier(HttpsUtils.DO_NOT_VERIFY);
		}
		httpsConn.setConnectTimeout(reqTimeout);
		httpsConn.setReadTimeout(resTimeout);
		httpsConn.setRequestMethod("POST");
		httpsConn.setDoInput(true);
		httpsConn.setDoOutput(true);
		httpsConn.setRequestProperty("Content-Type", "application/json");
		httpsConn.setRequestProperty("Referer",	MPConfig.CODE_ON_PAY_URL_REFERE);
		httpsConn.setRequestProperty("connection", "keep-alive");
		httpsConn.setRequestProperty("Charset", "UTF-8");
		OutputStream outStream = httpsConn.getOutputStream();
		outStream.write(byteData);
		try {
			httpsConn.connect();

			if (httpsConn.getResponseCode() == 200) {
				inStream = httpsConn.getInputStream();
				responseData = new String(StreamUtil.readStream(inStream),"UTF-8");
			} else {
				responseData = null;
			}
		} catch (SocketException e) {
			throw new DisconnectUrlException();
		}
		if (httpsConn != null) {
			httpsConn.disconnect();
		}
		return responseData;
	}
