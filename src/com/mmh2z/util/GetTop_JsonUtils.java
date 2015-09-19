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

import com.mmh2z.object.TopCourse;

public class GetTop_JsonUtils {

	public static List<TopCourse> getTop_JsonData(String url, String method) {
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
			Log.i("Test", result.toString());
			if (result.toString().isEmpty()) {
				return null;
			} else {
				List<TopCourse> list = Top_JsonParse(result.toString());
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<TopCourse> Top_JsonParse(String jsonData) {
		List<TopCourse> listItems = new ArrayList<TopCourse>();

		// Log.i("JsonDataHandler",jsonData);

		try {
			// 处理数据
			JSONObject object = new JSONObject(jsonData);
			// System.out.println(object.length());

			int flag = object.getInt("flag");

			if (flag != 1) {

				JSONArray subcateArray = object.getJSONArray("cate");

				for (int i = 0; i < subcateArray.length(); i++) {
					JSONObject json = subcateArray.getJSONObject(i);

					int cid = json.getInt("cid");
					int pid = json.getInt("pid");
					String name = json.getString("name");

					listItems.add(new TopCourse(cid, pid, name));
				}

				return listItems;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
