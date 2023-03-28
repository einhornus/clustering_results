public static String sendHttpRequest(String url, String jsonString){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		String responseString = null;
		try {
			StringEntity se = new StringEntity( jsonString);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			response = httpClient.execute(post);
			responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return responseString;
	}
--------------------

public static String sendPostRequest(String urlStr,Map<String, String> parmap, String charSet) {
		long begainTime = System.currentTimeMillis();
		HttpClient client = new HttpClient();
		// 设置超时时间 假如超时 则返回 ""
		 client.getHttpConnectionManager().getParams().setConnectionTimeout(15*1000);
		// 表示用Post方式提交
		PostMethod method = new PostMethod(urlStr);
		// 编码
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
		method.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla");
		
		// 设置请求参数
		if (null != parmap && parmap.size() > 0) {
			Iterator it = parmap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> me = (Map.Entry) it.next();
				method.addParameter(me.getKey(), me.getValue() == null ? "":me.getValue());
			}
		}
		try {
			int status = client.executeMethod(method);
			if (status == 200) {
				String rs = new String(method.getResponseBody(), charSet);
				return rs;
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;
	}
--------------------

public JSONObject getJSONFromUrl(final String url) {

		// Making HTTP request
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);

			// Executing POST request & storing the response from server  locally.
			HttpResponse httpResponse = httpClient.execute(httpPost);

			HttpEntity httpEntity = httpResponse.getEntity();

			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {


			// Create a BufferedReader
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			// Declaring string builder 
			StringBuilder str = new StringBuilder();
			//  string to store the JSON object.
			String strLine = null;

			// Building while we have string !equal null.
			while ((strLine = reader.readLine()) != null) {
				str.append(strLine + "\n");
			}

			// Close inputstream.
			is.close();
			// string builder data conversion  to string.
			json = str.toString();
		} catch (Exception e) {
			Log.e("Error", " something wrong with converting result " + e.toString());
		}

		// Try block used for pasrseing String to a json object
		try {
			jsonObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("json Parsering", "" + e.toString());
		}

		// Returning json Object.
		return jsonObj;

	}
--------------------

