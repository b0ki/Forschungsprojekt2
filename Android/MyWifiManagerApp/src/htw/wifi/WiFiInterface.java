package htw.wifi;

import java.util.List;

import android.net.wifi.ScanResult;

public interface WiFiInterface {

	public void onScannedWifi(List<ScanResult> results);
}
