package com.rightstrike.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rightstrike.R;
import com.rightstrike.bluetooth.BluetoothStarter;
import com.rightstrike.run.StrikeManager;

public class RunLoggerFragment extends Fragment {
	
	public static final String ARG_CURRENTLY_RUNNING = "in_progress";
	private static final String TAG = RunLoggerFragment.class.getSimpleName();
	
	private TextView mStatusText;
	private Button mToggleButton;
	private Button mCloseButton;
	private View mRootView;
	private boolean mRunning = false;
	private BluetoothStarter mStarter;
	private StrikeManager mStrikeManager;
	
	public static RunLoggerFragment getInstance(boolean running, BluetoothStarter starter) {
		RunLoggerFragment frag = new RunLoggerFragment();
		Bundle args = new Bundle();
		args.putBoolean(ARG_CURRENTLY_RUNNING, running);
		frag.setArguments(args);
		frag.mStarter = starter;
		return frag;
	}
	
	public RunLoggerFragment() {
		// Do nothing, for now
	}
	
	private OnClickListener mFinishListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setRunning(false);
			mStarter.stopReadingBluetooth();
			mStrikeManager.recordStrikes();
			mStrikeManager = null;
		}
		
	};
	
	private OnClickListener mStartListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setRunning(true);
			mStrikeManager = new StrikeManager(getActivity().getApplicationContext());
			mStarter.startReadingBluetooth(mStrikeManager);
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			Log.d(TAG, "savedInstanceState not null");
			mRunning = savedInstanceState.getBoolean(ARG_CURRENTLY_RUNNING, false);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_run_logger,
				container, false);
		mStatusText = (TextView) mRootView.findViewById(R.id.text_log_status);
		mToggleButton = (Button) mRootView.findViewById(R.id.button_toggle_log);
		mCloseButton = (Button) mRootView.findViewById(R.id.button_close_app);
		mCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRunning) {
					setRunning(false);
					mStarter.stopReadingBluetooth();
					mStrikeManager.recordStrikes();
					mStrikeManager = null;
				}
				RunLoggerFragment.this.getActivity().finish();
			}
			
		});
		setRunning(mRunning);
		return mRootView;
	}
	
	/**
	 * Do not call before the call to onCreateView.
	 * 
	 * @param running {@code true} if currently running
	 */
	public void setRunning(boolean running) {
		// Update the layout states
		if (running) {
			mToggleButton.setOnClickListener(mFinishListener);
			mToggleButton.setText(R.string.button_end_log);
			mStatusText.setText(R.string.log_status_running);
		} else {
			mToggleButton.setOnClickListener(mStartListener);
			mToggleButton.setText(R.string.button_start_log);
			mStatusText.setText(R.string.log_status_stopped);
		}
		mRootView.invalidate();
		// Update internal state
		mRunning = running;
	}
}
