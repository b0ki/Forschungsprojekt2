package htw.bluetooth;

public class RemoteBluetoothDevice extends BluetoothService implements Comparable<RemoteBluetoothDevice> {

	// Name of remote device
	private final String mName;
	
	// Address of remote device
	private final String mAddress;
	
	// Signal strength of remote device
	private final short mRSSI;
	
	public RemoteBluetoothDevice(String name, String address, short rssi) {
		mName = name;
		mAddress = address;
		mRSSI = rssi;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getAddress() {
		return mAddress;
	}
	
	public short getRSSI() {
		return mRSSI;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if ( !(o instanceof RemoteBluetoothDevice) ) {
			return false;
		}
		
		RemoteBluetoothDevice other= (RemoteBluetoothDevice) o;
		
		return (other.getAddress().equalsIgnoreCase(this.getAddress()));
	}

	@Override
	public int compareTo(RemoteBluetoothDevice another) {
		if (this.getRSSI() < another.getRSSI()) {
			return 1;
		}
		
		if (this.getRSSI() > another.getRSSI()) {
			return -1;
		}
		
		return 0;
	}
}
