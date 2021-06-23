package com.hkzr.wlwd.httpUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.util.Log;

/**
 * Created by @author 951882080@qq.com on 2016/2/19.
 */
public class FileUploadReq {

	private Map<String, String> params;

	private FormFile[] files;

	final String BOUNDARY = "---------------------------7da2137580612";
	final String endline = "--" + BOUNDARY + "--\r\n";

	final int TIME = 30000;

	private String path;

	public FileUploadReq(String url, Map<String, String> params,
			FormFile[] files) {
		this.params = params;
		this.files = files;
		path = url;
	}

	public String post() throws IOException {
		URL url = new URL(path);
		HttpURLConnection urlConnection = (HttpURLConnection) url
				.openConnection();
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		urlConnection.setConnectTimeout(TIME);
		urlConnection.setReadTimeout(TIME);
		urlConnection.setRequestMethod("POST");
		urlConnection.setRequestProperty("Content-Type",  
	                "multipart/form-data; boundary=" + BOUNDARY); 

		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"\r\n\r\n");
			textEntity.append(entry.getValue());
			textEntity.append("\r\n");
		}
		OutputStream out = new BufferedOutputStream(
				urlConnection.getOutputStream());

		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				out);

		bufferedOutputStream.write(textEntity.toString().getBytes());

		for (FormFile uploadFile : files) {
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("\r\n");
			fileEntity.append("Content-Disposition: form-data;name=\""
					+ uploadFile.getParameterName() + "\";filename=\""
					+ uploadFile.getFilname() + "\"\r\n");
			fileEntity.append("Content-Type: " + uploadFile.getContentType()
					+ "\r\n\r\n");
			bufferedOutputStream.write(fileEntity.toString().getBytes());
			if (uploadFile.getInStream() != null) {
				byte[] buffer = new byte[1024 * 8];
				int len = 0;
				while ((len = uploadFile.getInStream().read(buffer)) != -1) {
					bufferedOutputStream.write(buffer, 0, len);
				}
				uploadFile.getInStream().close();
			} else {
				bufferedOutputStream.write(uploadFile.getData(), 0,
						uploadFile.getData().length);
			}
			bufferedOutputStream.write("\r\n".getBytes());
			Log.e(BOUNDARY, "end to write file!");
		}

		bufferedOutputStream.write(endline.getBytes());
		bufferedOutputStream.flush();
		Log.e(BOUNDARY, "begin to read!");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));
		Log.e(BOUNDARY, "get the back!");
		StringBuilder sb = new StringBuilder();
		String d;
		while ((d = reader.readLine()) != null) {
			sb.append(d);
		}
		Log.e(BOUNDARY, "end to connect!");	
		bufferedOutputStream.close();
		reader.close();
		urlConnection.disconnect();
		return sb.toString();
	}

}
