package de.htw;



import java.util.Collections;
import java.util.List;

import de.htw.bluetooth.BluetoothInterface;
import de.htw.bluetooth.BluetoothService;
import de.htw.bluetooth.RemoteBluetoothDevice;
import de.htw.bluetooth.BluetoothService.LocalBluetoothBinder;
import de.htw.wifi.*;
import de.htw.wifi.WiFiService.LocalWiFiBinder;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ForschungsprojektAppActivity extends Activity implements BluetoothInterface, WiFiInterface {

	private static final String LOG_TAG = "ForschungsprojektAppActivity";
	
	private ToggleButton mBluetoothToggle;
	
	// Service State
	private BluetoothService mBluetoothService;
	private WiFiService mWiFiService;
    private boolean isBluetoothServiceBound;
    private boolean isWiFiServiceBound;

    // Bluetooth
	private BluetoothAdapter mBluetoothAdapter;
	private int REQUEST_ENABLE_BT = 0;

	private TextView mBluetoothQuerries;

	private ToggleButton mWiFiScan;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mBluetoothToggle = (ToggleButton) findViewById(R.id.toggle_bluetooth);
        mBluetoothToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mBluetoothToggle.isChecked()) {
					Intent service = new Intent(ForschungsprojektAppActivity.this, BluetoothService.class);
	        		bindService(service, mBluetoothConnection, Context.BIND_AUTO_CREATE); 
	        		isBluetoothServiceBound = true;
				} else {
					mBluetoothService.unregisterCallback(ForschungsprojektAppActivity.this);
			    	unbindService(mBluetoothConnection);
			    	isBluetoothServiceBound = false;
				}
				
			}
		});
        
        setupBluetooth();
        
        
        mWiFiScan = (ToggleButton) findViewById(R.id.button_wifi_scan);
        mWiFiScan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if (mWiFiScan.isChecked()) {
					Intent service = new Intent(ForschungsprojektAppActivity.this, WiFiService.class);
					bindService(service, mWiFiConnection, Context.BIND_AUTO_CREATE);
					isWiFiServiceBound = true;
				} else {
					unbindService(mWiFiConnection);
					isWiFiServiceBound = false;
				}
			}
		});
    }
    
    /**
     * Setup the Bluetooth Access
     */
    private void setupBluetooth() {
    	// The BluetoothAdapter is required for any and all Bluetooth activity
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //  If getDefaultAdapter() returns null, then the device does not support Bluetooth
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        
        // Call isEnabled() to check whether Bluetooth is currently enable
        if (!mBluetoothAdapter.isEnabled()) {
        	// This will issue a request to enable Bluetooth through the system settings
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
        	if (mBluetoothToggle.isChecked()) {
        		Intent service = new Intent(ForschungsprojektAppActivity.this, BluetoothService.class);
        		bindService(service, mBluetoothConnection, Context.BIND_AUTO_CREATE);        
        		isBluetoothServiceBound = true;
        	}
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if (resultCode == RESULT_OK) {
    		Log.d(LOG_TAG, "RESULT_OK, result code: ");
    		
    		if (mBluetoothToggle.isChecked()) {
    			// bind to service when Bluetooth has been activated
    			Intent service = new Intent(ForschungsprojektAppActivity.this, BluetoothService.class);
    			bindService(service, mBluetoothConnection, Context.BIND_AUTO_CREATE);
    			isBluetoothServiceBound = true;
    		}
    	}
    	
    	if (resultCode == RESULT_CANCELED) {
    		Log.d(LOG_TAG, "RESULT_CANCELED");
    	}
    }


	@Override
	public void onScannedBluetoothDevices(List<RemoteBluetoothDevice> devices) {
		Log.d(LOG_TAG, "Bluetooth Networks: ");
		
		// sortiere Liste
		Collections.sort(devices);
		
		StringBuffer buf = new StringBuffer();
		for (RemoteBluetoothDevice device : devices) {
    		Log.d(LOG_TAG, "Address: "+ device.getAddress() + " | RSSI: " + device.getRSSI() + " | Name: " + device.getName());
    		//if (device.getAddress().equalsIgnoreCase("44:2A:60:DA:61:44")) {
    			buf.append(device.getName() + " | " + device.getAddress() + " |Ê" + device.getRSSI()+ "\n");    			
    		//}
    	}
		
		
		
		//mBluetoothQuerries.setText(buf.toString());
		
	}
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mBluetoothConnection = new ServiceConnection() {

		@Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
				Log.d(LOG_TAG, "connect to Bluetooth service");
				// We've bound to LocalService, cast the IBinder and get LocalService instance
				LocalBluetoothBinder binder = (LocalBluetoothBinder) service;
				mBluetoothService = binder.getService();
				mBluetoothService.registerCallback(ForschungsprojektAppActivity.this);
				mBluetoothService.setSleepInterval(10000);				
			
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG, "disconnect from service");
        }
    };
    
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mWiFiConnection = new ServiceConnection() {

		@Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
				Log.d(LOG_TAG, "connect to WiFi service");
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	            LocalWiFiBinder binder = (LocalWiFiBinder) service;
	            mWiFiService = binder.getService();
	            mWiFiService.registerCallback(ForschungsprojektAppActivity.this);
	            mWiFiService.setSleepInterval(10000);
	            //mService.setSleepInterval(10000);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG, "disconnect from service");
        }
    };
    
    
    @Override
    protected void onDestroy() {
    	if (isBluetoothServiceBound) {
    		mBluetoothService.unregisterCallback(this);
    		unbindService(mBluetoothConnection);    		
    	}
    	
    	if (isWiFiServiceBound) {
    		mWiFiService.unregisterCallback(this);
    		unbindService(mWiFiConnection);
    	}
    	
    	super.onDestroy();
    }

	@Override
	public void onScannedWifi(List<ScanResult> results) {
		Log.d(LOG_TAG, "WiFi Networks:");
		Collections.sort(results, new WiFiComparator());
		
		StringBuffer buf = new StringBuffer();
		for (ScanResult result : results) {
			Log.d(LOG_TAG, "BSSID: " + result.BSSID + " |ÊSignal-Strength: "+ result.level + " |ÊSSID: " + result.SSID);
			buf.append(result.SSID + "Ê|Ê" + result.BSSID + " |Ê" + result.level + "\n");
		}
		
	}
}