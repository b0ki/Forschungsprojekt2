package de.htw.bluetooth;

import java.util.ArrayList;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Search for other Bluetooth devices within a service.
 * 
 * @author Christian Bunk
 *
 */
public class BluetoothService extends Service implements Runnable {

	private static final String LOG_TAG = "BluetoothService";

	private BluetoothAdapter mBluetoothAdapter;
	
	// Contain the found Bluetooth devices
	private ArrayList<RemoteBluetoothDevice> mFoundBluetoothDevices;
	
	// Contain all Callbacks
	private ArrayList<BluetoothInterface> mCallbacks;
	
	// time to sleep between bluetooth discovery
	private int mSleepInterval = 5000;
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	//Log.d(LOG_TAG, "divice has been found");
	        	
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            
	            // Get the signal strength
	            short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
	            
	            // Add the name and address to an array adapter to show in a ListView
	            RemoteBluetoothDevice newDevice = new RemoteBluetoothDevice(device.getName(), device.getAddress(), rssi);
	            if (!mFoundBluetoothDevices.contains(newDevice)) {
	            	mFoundBluetoothDevices.add(newDevice);	            	
	            }
	        }
	        
	        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	//Log.d(LOG_TAG, "Suche abgeschlossen. Gefundene Geräte: " + mFoundBluetoothDevices.size());
	        	
	        	/*for (BluetoothDevice device : mFoundBluetoothDevices) {
	        		Log.d(LOG_TAG, "Address: "+ device.getAddress()+ " Name: " + device.getName());
	        	}*/
	        	
	        	notifyCallbacks();
	        	
	        	
	        	
	        	//stopSelf(mStartID);
	        	
	        	Thread t = new Thread(BluetoothService.this);
	        	t.start();
	        }
	    }
	};
	
	  @Override
	  public void onCreate() {
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    //HandlerThread thread = new HandlerThread("ServiceStartArguments",
	    //        Process.THREAD_PRIORITY_BACKGROUND);
	    //thread.start();
	    
	    // Get the HandlerThread's Looper and use it for our Handler 
	    //mServiceLooper = thread.getLooper();
	    //mServiceHandler = new ServiceHandler(mServiceLooper);
	    
	    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    mFoundBluetoothDevices = new ArrayList<RemoteBluetoothDevice>();
	    
	    // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        
        Log.d(LOG_TAG, "create service");
        
        mCallbacks = new ArrayList<BluetoothInterface>();
	  }
	  
	  /**
	   * Register a Callback.
	   * @param callback
	   */
	  public void registerCallback(BluetoothInterface callback) {
		  mCallbacks.add(callback);
	  }
	  
	  /**
	   * Unregister a callback.
	   * @param callback
	   */
	  public void unregisterCallback(BluetoothInterface callback) {
		  mCallbacks.remove(callback);
	  }
	  
	  /**
	   * Notify all Callbacks.
	   */
	  private void notifyCallbacks() {
		  for (BluetoothInterface callback : mCallbacks) {
			  callback.onScannedBluetoothDevices(mFoundBluetoothDevices);
		  }
	  }
	  
	  /**
	   * Set the sleep interval between bluetooth discovery.
	   * @param interval
	   */
	  public void setSleepInterval(int interval) {
		  if (interval >= 1000) {
			  mSleepInterval = interval;			  
		  }
	  }

	  // Binder given to clients
	  private final IBinder mBinder = new LocalBluetoothBinder();

	  @Override
	  public IBinder onBind(Intent intent) {
		  Log.d(LOG_TAG, "onBind");
		  mBluetoothAdapter.startDiscovery();
	      // We don't provide binding, so return null
		  return mBinder;
	  }
	  
	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
	    unregisterReceiver(mReceiver);
	    mBluetoothAdapter.cancelDiscovery();
	    Log.d(LOG_TAG, "destroy service");
	    super.onDestroy();
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
		
		mFoundBluetoothDevices.clear();
		mBluetoothAdapter.startDiscovery();
		
	}
	
	/**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBluetoothBinder extends Binder {
        public BluetoothService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BluetoothService.this;
        }
    }
}


