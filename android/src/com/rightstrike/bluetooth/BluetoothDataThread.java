package com.rightstrike.bluetooth;

import java.io.IOException;
import java.io.InputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothDataThread extends Thread {

	private static final String TAG = BluetoothDataThread.class.getSimpleName();

	private InputStream mInput;
	private volatile boolean mShouldRead;
	private RunDataHandler mDataHandler;

	public BluetoothDataThread(BluetoothSocket socket, RunDataHandler handler)
			throws BluetoothException {
		mDataHandler = handler;
		try {
			mInput = socket.getInputStream();
		} catch (IOException e) {
			throw new BluetoothException(e);
		}
		mShouldRead = true;
	}

	public synchronized void stopListening() {
		mShouldRead = false;
	}

	@Override
	public void run() {
		Log.v(TAG, "Worker thread running...");
		while (mShouldRead) {
			try {
				int data = mInput.read();
				Log.d(TAG, Integer.toString(data));
				mDataHandler.handle(data);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
}
