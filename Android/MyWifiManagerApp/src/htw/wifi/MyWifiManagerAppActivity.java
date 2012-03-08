package htw.wifi;

import htw.wifi.WiFiService.LocalBinder;

import java.util.Collections;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
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

	private TextView mTextWifiResults;
    
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
        
        mTextWifiResults = (TextView) findViewById(R.id.text_wifi_scan_results);
 
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
		Collections.sort(results, new WiFiComparator());
		
		StringBuffer buf = new StringBuffer();
		for (ScanResult result : results) {
			Log.d(LOG_TAG, "BSSID: " + result.BSSID + " |ÊSignal-Strength: "+ result.level + " |ÊSSID: " + result.SSID);
			buf.append(result.SSID + "Ê|Ê" + result.BSSID + " |Ê" + result.level + "\n");
		}	
		
		mTextWifiResults.setText(buf.toString());
	}
}