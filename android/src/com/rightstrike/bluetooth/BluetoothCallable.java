package com.rightstrike.bluetooth;

import android.bluetooth.BluetoothSocket;

public interface BluetoothCallable {

	public void onSuccessfulConnection(BluetoothSocket socket);
	public void onFailedConnection();
}
