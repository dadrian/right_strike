package com.rightstrike.run;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.rightstrike.R;
import com.rightstrike.bluetooth.RunDataHandler;
import com.rightstrike.http.HttpManager;

public class StrikeManager implements RunDataHandler {

	private static final String TAG = StrikeManager.class.getSimpleName();

	private ArrayList<StrikeType> mStrikes;
	private DateTime mStartTime;

	private boolean ignore = false;
	private int ignoreCount = 0;
	private int countBad = 0;
	private Context context;
	private MediaPlayer player;

	public static final int FRONT = 65;
	public static final int BACK = 90;
	public static final int IGNORE_THRESHOLD = 3;
	public static final int ALERT_THRESHOLD = 3;

	public StrikeManager(Context context) {
		this.context = context;
		mStrikes = new ArrayList<StrikeType>();
		mStartTime = new DateTime();
		ignore = true;
		ignoreCount = 0;
	}

	@Override
	public void handle(int nextByte) {
		synchronized (this) {
			Log.d(TAG, "Handle called on byte " + Integer.toString(nextByte));
			if (mStrikes == null) {
				return;
			}
			// Ignore the start of the stream
			if (ignore) {
				Log.d(TAG, "Ignored the data");
				ignoreCount++;
				if (ignoreCount >= IGNORE_THRESHOLD) {
					ignore = false;
				}
				return;
			}
			switch (nextByte) {
			case (FRONT):
				mStrikes.add(StrikeType.FRONT);
				countBad = 0;
				Log.d(TAG, "Logged a front");
				break;
			case (BACK):
				mStrikes.add(StrikeType.HEEL);
				countBad++;
				Log.d(TAG, "Logged a back " + Integer.toString(countBad));
				if (countBad >= ALERT_THRESHOLD) {
					alertBadForm();
				}
				break;
			default:
				// Junk data, do nothing
				break;
			}
		}
	}

	public void alertBadForm() {
		countBad = 0;
		if (player == null) {
			player = MediaPlayer.create(context, R.raw.alert);
		}
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				if (!player.isPlaying()) {
					player.start();
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public void recordStrikes() {
		synchronized (this) {
			if (mStrikes.size() > 0) {
				Log.d(TAG, "Calling postNewRun from recordStrikes");
				HttpManager.postNewRun(mStartTime, mStrikes);
			}
			mStrikes = null;
		}
	}

}
