package com.rightstrike.bluetooth;

import java.io.IOException;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import com.rightstrike.Config;

public class BluetoothConnectTask extends
		AsyncTask<BluetoothAdapter, Void, BluetoothSocket> {

	public static final String TAG = BluetoothConnectTask.class.getSimpleName();
	
	private BluetoothCallable mCallback;
	
	public BluetoothConnectTask(BluetoothCallable callback) {
		mCallback = callback;
	}

	@Override
	protected BluetoothSocket doInBackground(BluetoothAdapter... adapter) {
		Set<BluetoothDevice> pairedDevices = adapter[0].getBondedDevices();
		adapter[0].cancelDiscovery();
		for (BluetoothDevice device : pairedDevices) {
			if (device.getName().matches(Config.DEVICE_NAME)) {
				BluetoothSocket socket;
				// Create the socket
				try {
					socket = device
							.createRfcommSocketToServiceRecord(Config.BLUETOOTH_UUID);
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					return null;
				}
				// Not sure if this is possible, but may as well check
				if (socket.isConnected()) {
					return socket;
				}
				// Connect to the socket
				try {
					socket.connect();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					try {
						socket.close();
					} catch (IOException e1) {
						// Do nothing
					}
					return null;
				}
				// Return connected socket
				Log.v(TAG, "Connected!");
				return socket;
			}
		}
		Log.e(TAG, "Could not find correct bluetooth device");
		return null;
	}

	@Override
	protected void onPostExecute(BluetoothSocket result) {
		super.onPostExecute(result);
		if (result == null || !result.isConnected()) {
			mCallback.onFailedConnection();
		} else {
			mCallback.onSuccessfulConnection(result);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

}
