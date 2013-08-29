package com.rightstrike.bluetooth;

public interface BluetoothStarter {
	
	public boolean startReadingBluetooth(RunDataHandler handler);
	public void stopReadingBluetooth();

}
