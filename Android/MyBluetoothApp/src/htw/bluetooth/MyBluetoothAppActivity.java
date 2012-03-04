package htw.bluetooth;

import java.util.List;

import htw.bluetooth.BluetoothService.LocalBinder;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyBluetoothAppActivity extends Activity implements BluetoothInterface {
	
	private static final String LOG_TAG = "MyBluetoothAppActivity";

	private int REQUEST_ENABLE_BT = 0;
	
	private BluetoothService mService;


	private BluetoothAdapter mBluetoothAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
        	Intent service = new Intent(MyBluetoothAppActivity.this, BluetoothService.class);
	        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
        }
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(mConnection);
    	mService.unregisterCallback(this);
    	//unregisterReceiver(mReceiver);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if (resultCode == RESULT_OK) {
    		Log.d(LOG_TAG, "RESULT_OK, result code: ");
    		
    		Intent service = new Intent(MyBluetoothAppActivity.this, BluetoothService.class);
        	bindService(service, mConnection, Context.BIND_AUTO_CREATE); 
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
            mService.setSleepInterval(10000);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(LOG_TAG, "disconnect from service");
        }
    };

	@Override
	public void onScannedBluetoothDevices(List<BluetoothDevice> devices) {
		Log.d(LOG_TAG, "Aktualisiert Liste Bluetooth Geräte.");
		for (BluetoothDevice device : devices) {
    		Log.d(LOG_TAG, "Address: "+ device.getAddress()+ " Name: " + device.getName());
    	}
		
	}
}