package de.htw.wifi;

import java.util.Comparator;

import android.net.wifi.ScanResult;

public class WiFiComparator implements Comparator<ScanResult> {

	@Override
	public int compare(ScanResult object1, ScanResult object2) {
		if (object1.level < object2.level) {
			return 1;
		}
		
		if (object1.level > object2.level) {
			return -1;
		}
		
		return 0;
	}

}
