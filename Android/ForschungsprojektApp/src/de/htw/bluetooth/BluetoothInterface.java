package de.htw.bluetooth;

import java.util.List;

public interface BluetoothInterface {

	public void onScannedBluetoothDevices(List<RemoteBluetoothDevice> devices);
}
