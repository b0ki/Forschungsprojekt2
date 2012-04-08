package de.htw.light;

import java.util.ArrayList;
import java.util.List;

import de.htw.bluetooth.BluetoothInterface;
import de.htw.bluetooth.BluetoothService;
import de.htw.bluetooth.BluetoothService.LocalBluetoothBinder;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LightService extends Service implements SensorEventListener {

	private static final String LOG_TAG = "LightSerice";
	
	private Sensor mLight;
	private SensorManager mSensorManager;

	private LightInterface mCallback;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.d(LOG_TAG, "create LightService");
		
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onDestroy() {
		mSensorManager.unregisterListener(this);
		Log.d(LOG_TAG, "destroy LightService");
		
		super.onDestroy();
	}
	
	/**
	   * Register a Callback.
	   * @param callback
	   */
	  public void registerCallback(LightInterface callback) {
		  Log.d(LOG_TAG, "set callback");
		  mCallback = callback;
	  }
	  

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Sensor.TYPE_LIGHT == event.sensor.getType()) {
			
			if (mCallback != null) {
				mCallback.onScannedLight((int) event.values[0]);				
			} else {
				Log.d(LOG_TAG, "callback is null");
			}
		}
		
	}
	
	// Binder given to clients
	private final IBinder mBinder = new LocalLightBinder();
	
	@Override
	  public IBinder onBind(Intent intent) {
		  Log.d(LOG_TAG, "onBind");
	      // We don't provide binding, so return null
		  return mBinder;
	  }
	
	/**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalLightBinder extends Binder {
        public LightService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LightService.this;
        }
    }

}
