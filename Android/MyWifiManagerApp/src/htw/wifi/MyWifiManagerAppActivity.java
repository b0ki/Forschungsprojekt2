package htw.wifi;

import htw.wifi.WiFiService.LocalBinder;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MyWifiManagerAppActivity extends Activity implements WiFiInterface {
	
	private static final String LOG_TAG = "MyWiFiManagerAppActivity";
	
	private boolean mServiceBound = false;
	
	//private WifiManager wifi;
	private ToggleButton mWiFiScan;
	
	private WiFiService mService;
	
	/** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

		@Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
			
			Log.d(LOG_TAG, "connect to service");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mService.registerCallback(MyWifiManagerAppActivity.this);
            //mService.setSleepInterval(10000);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG, "disconnect from service");
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mWiFiScan = (ToggleButton) findViewById(R.id.button_wifi_scan);
        mWiFiScan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if (mWiFiScan.isChecked()) {
					Intent service = new Intent(MyWifiManagerAppActivity.this, WiFiService.class);
					bindService(service, mConnection, Context.BIND_AUTO_CREATE);
					mServiceBound = true;
				} else {
					unbindService(mConnection);
					mServiceBound = false;
				}
			}
		});
 
    }
    
    @Override
	public void onStop() {
		
    	if (mServiceBound) {
    		mService.unregisterCallback(this);
    		unbindService(mConnection);    		
    	}
    	
    	super.onStop();
	}

	@Override
	public void onScannedWifi(List<ScanResult> results) {
		for (ScanResult result : results) {
			Log.d(LOG_TAG, "BSSID: " + result.BSSID + " |ÊSignal-Strength: "+ result.level + " |ÊSSID: " + result.SSID);
		}
		
	}
}