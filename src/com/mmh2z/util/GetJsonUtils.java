package com.mmh2z.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.mmh2z.object.Course;

public class GetJsonUtils {

	public static List<Course> getJsonData(String url, String method) {
		HttpURLConnection conn;
		InputStream is;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod(method);
			is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = "";
			StringBuilder result = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				result.append(line);
			}
//			Log.i("Test", result.toString());
			if (result.toString().isEmpty()) {
				return null;
			} else {
				List<Course> list = JsonParse(result.toString());
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<Course> JsonParse(String jsonData) {
		List<Course> listItems = new ArrayList<Course>();

//		Log.i("JsonDataHandler",jsonData);
		
		try {
			// 处理数据
			JSONArray object = new JSONArray(jsonData);

			for (int i = 0; i < object.length(); i++) {
				JSONObject json = object.getJSONObject(i);

				int id = json.getInt("id");
				String name = json.getString("name");
				String imageurl = json.getString("image");
				int pid=json.getInt("pid");
				int  cid = json.getInt("cid");
				int state = json.getInt("state");

				listItems.add(new Course(id, cid,pid, imageurl, name, state));
			}

			return listItems;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
