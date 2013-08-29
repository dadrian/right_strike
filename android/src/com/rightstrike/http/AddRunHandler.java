package com.rightstrike.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import android.util.Log;

public class AddRunHandler implements ResponseHandler<Boolean> {
	
	private static final String TAG = AddRunHandler.class.getSimpleName();

	@Override
	public Boolean handleResponse(HttpResponse response)
			throws ClientProtocolException, IOException {
		if (response == null) return false;
		int status = response.getStatusLine().getStatusCode();
		if (status == HttpStatus.SC_OK) {
			return true;
		} else {
			Log.e(TAG, "Status was: " + Integer.toString(status));
		}
		return false;
	}

}
