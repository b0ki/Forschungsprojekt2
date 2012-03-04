package htw.bluetooth;

import java.util.List;

import android.bluetooth.BluetoothDevice;

public interface BluetoothInterface {

	public void onScannedBluetoothDevices(List<BluetoothDevice> devices);
}
