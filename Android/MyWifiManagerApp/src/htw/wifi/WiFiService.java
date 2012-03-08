package htw.wifi;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class WiFiService extends Service implements Runnable {
	
	private static final String LOG_TAG = "WiFiService";

	private WifiManager wifi;
	private int mSleepInterval = 5000;
	private List<ScanResult> mWiFiNetworks;
	
	private List<WiFiInterface> mCallbacks;

	
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
				
			mWiFiNetworks = wifi.getScanResults();
			
			Log.d(LOG_TAG, "Wlan Networks found: ");
			
			notifyCallbacks();
			
			// -1 means no connection
			if (wifi.getConnectionInfo().getNetworkId() != -1) {
				Thread t = new Thread(WiFiService.this);
				t.start();
			}
		}
	};

	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "create service");
		
		mWiFiNetworks = new ArrayList<ScanResult>();
		
		// Setup WiFi
     	wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
     	
     	// Enable WiFi
     	if (!wifi.isWifiEnabled()) {
     		wifi.setWifiEnabled(true);
     	}
     	
     	
     	registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
     	// Starte Scann
     	// TODO Kann es sein das wenn das Smartphone mit keinem Wlan Acces Point verbunden ist das Gerät permanent sucht?
     	
     	// -1 means no connection
     	if (wifi.getConnectionInfo().getNetworkId() != -1) {
     		wifi.startScan();
     	}
     	
     	mCallbacks = new ArrayList<WiFiInterface>();
	}
		
	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind");
		return mBinder;
	}
	
	@Override
	public void onDestroy() {
		Log.d(LOG_TAG, "destroy service");
		unregisterReceiver(receiver);
		mCallbacks.clear();
		
		super.onDestroy();
	}
	
	/**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        WiFiService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WiFiService.this;
        }
    }

	@Override
	public void run() {
		try {
			Log.d(LOG_TAG, "Service sleep for " + mSleepInterval/1000 + " seconds");
			Thread.sleep(mSleepInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mWiFiNetworks.clear();
		wifi.startScan();
		//mBluetoothAdapter.startDiscovery();
		
	}

	public void registerCallback(WiFiInterface callback) {
		mCallbacks.add(callback);
	}
	
	public void unregisterCallback(WiFiInterface callback) {
		mCallbacks.remove(callback);
	}
	
	private void notifyCallbacks() {
		for (WiFiInterface callback : mCallbacks) {
			callback.onScannedWifi(mWiFiNetworks);
		}
	}
}

