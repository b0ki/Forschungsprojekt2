package htw.bluetooth;

import htw.bluetooth.BluetoothService.LocalBinder;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MyBluetoothAppActivity extends Activity implements BluetoothInterface {
	
	private static final String LOG_TAG = "MyBluetoothAppActivity";

	private int REQUEST_ENABLE_BT = 0;
	
	private BluetoothService mService;
	private BluetoothAdapter mBluetoothAdapter;

	private ToggleButton mBluetoothToggle;
	
	private boolean isServiceBound = false;

	private TextView mBluetoothQuerries;
	
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
					Intent service = new Intent(MyBluetoothAppActivity.this, BluetoothService.class);
	        		bindService(service, mConnection, Context.BIND_AUTO_CREATE); 
	        		isServiceBound = true;
				} else {
					mService.unregisterCallback(MyBluetoothAppActivity.this);
			    	unbindService(mConnection);
			    	isServiceBound = false;
				}
				
			}
		});
        
        mBluetoothQuerries = (TextView) findViewById(R.id.text_querry);
        setupBluetooth(); 
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
        		Intent service = new Intent(MyBluetoothAppActivity.this, BluetoothService.class);
        		bindService(service, mConnection, Context.BIND_AUTO_CREATE);        
        		isServiceBound = true;
        	}
        }
    }
    
    @Override
    protected void onDestroy() {
    	if (isServiceBound) {
    		mService.unregisterCallback(this);
    		unbindService(mConnection);    		
    	}
    	super.onDestroy();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if (resultCode == RESULT_OK) {
    		Log.d(LOG_TAG, "RESULT_OK, result code: ");
    		
    		if (mBluetoothToggle.isChecked()) {
    			// bind to service when Bluetooth has been activated
    			Intent service = new Intent(MyBluetoothAppActivity.this, BluetoothService.class);
    			bindService(service, mConnection, Context.BIND_AUTO_CREATE);
    			isServiceBound = true;
    		}
    	}
    	
    	if (resultCode == RESULT_CANCELED) {
    		Log.d(LOG_TAG, "RESULT_CANCELED");
    	}
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

		@Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
			
			Log.d(LOG_TAG, "connect to service");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mService.registerCallback(MyBluetoothAppActivity.this);
            mService.setSleepInterval(1000);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG, "disconnect from service");
        }
    };

	@Override
	public void onScannedBluetoothDevices(List<RemoteBluetoothDevice> devices) {
		Log.d(LOG_TAG, devices.size() + " gefunden(e) Bluetooth Geräte");
		
		Log.d(LOG_TAG, "sortiere liste");
		// sortiere Liste
		Collections.sort(devices);
		
		StringBuffer buf = new StringBuffer();
		for (RemoteBluetoothDevice device : devices) {
    		Log.d(LOG_TAG, "Address: "+ device.getAddress() + " | RSSI: " + device.getRSSI() + " | Name: " + device.getName());
    		//if (device.getAddress().equalsIgnoreCase("44:2A:60:DA:61:44")) {
    			buf.append(device.getName() + " | " + device.getAddress() + " | " + device.getRSSI()+ "\n");    			
    		//}
    	}
		
		
		
		mBluetoothQuerries.setText(buf.toString());
	}
}