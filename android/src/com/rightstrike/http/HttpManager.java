package com.rightstrike.http;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;

import android.util.Log;

import com.rightstrike.run.StrikeType;

public class HttpManager {

	private static HttpClient client = null;
	private static final String TAG = HttpManager.class.getSimpleName();

	private static void initIfNeeded() {
		// WEE SINGLETONS
		if (client == null) {
			client = new DefaultHttpClient();
		}
	}
	
	private static class PostNewRunTask implements Runnable {

		private DateTime mStartTime;
		private ArrayList<StrikeType> mStepList;
		
		public PostNewRunTask(DateTime start, ArrayList<StrikeType> steps) {
			mStartTime = start;
			mStepList = steps;
		}
		
		@Override
		public void run() {
			AddRunRequest request = new AddRunRequest(mStartTime, mStepList);
			boolean success = false;
			long delay = 1;
			while (!success) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					// Do nothing
				}
				if (delay < 60000) {
					delay *= 2;
				}
				try {
					Log.d(TAG, "About to execute post request after delay of " + Long.toString(delay));
					success = client.execute(request, new AddRunHandler());
				} catch (ClientProtocolException e) {
					// Do nothing, not thrown
				} catch (IOException e) {
					// Do nothing, not thrown
				}
			}
			Log.v(TAG, "Successfully posted a run from: " + mStartTime.toString());
		}
		
	}

	public static void postNewRun(DateTime start, ArrayList<StrikeType> steps) {
		initIfNeeded();
		// Do this shit in the background
		PostNewRunTask task = new PostNewRunTask(start, steps);
		Thread thread = new Thread(task);
		thread.start();
	}
}
