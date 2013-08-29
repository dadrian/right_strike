package com.rightstrike.bluetooth;

import java.io.IOException;

public class BluetoothException extends IOException {

	/**
	 * Generated serial version UID
	 */
	private static final long serialVersionUID = 6241160617009153363L;
	
	public BluetoothException(IOException e) {
		super(e.getMessage(), e.getCause());
	}

}
