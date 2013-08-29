package com.rightstrike.http;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.rightstrike.Config;
import com.rightstrike.run.StrikeType;

public class AddRunRequest extends HttpPost {
	
	private static final String URL = Config.API_HOST_URL + "/runs";

	private static final String KEY_TIME = "date";
	private static final String TAG = AddRunRequest.class.getSimpleName();
	private static final String KEY_STEPS = "data";

	private static final String KEY_COUNT = "count";

	AddRunRequest(DateTime startingTime, Collection<StrikeType> steps) {
		super(URL);
		try {
			JSONObject body = new JSONObject();
			JSONArray stepArray = new JSONArray(steps);
			body.put(KEY_TIME, startingTime.getMillis());
			body.put(KEY_STEPS, stepArray);
			body.put(KEY_COUNT, steps.size());
			StringEntity entity = new StringEntity(body.toString());
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			this.setEntity(entity);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			throw new IllegalStateException(e);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
			throw new IllegalStateException(e);
		}
	}

}
