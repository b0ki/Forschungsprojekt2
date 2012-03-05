package htw.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MySensorAppActivity extends Activity implements SensorEventListener {
	
	private static final String LOG_TAG = "MySensorAppActivity";
	
    private SensorManager mSensorManager;
    
	private Sensor mLight;
	private Sensor mPressure;
	private Sensor mProximity;
	private Sensor mLinearAcceleration;

	private Sensor mMagneticField;
	private float[] mGeomagnetic;

	private Sensor mAccelerometer;
	private float[] mGravity;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    
    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (Sensor.TYPE_LIGHT == event.sensor.getType()) {
			Log.d(LOG_TAG, "light values" + event.values[0]);
		}
		
		if (Sensor.TYPE_PRESSURE == event.sensor.getType()) {
			Log.d(LOG_TAG, "pressure values" + event.values[0]);
		}
		
		
		if (Sensor.TYPE_PROXIMITY == event.sensor.getType()) {
			Log.d(LOG_TAG, "proximity values" + event.values[0]);
		}
		
		if (Sensor.TYPE_LINEAR_ACCELERATION == event.sensor.getType()) {
			Log.d(LOG_TAG, "linear acceleration values[0]" + event.values[0]);
			Log.d(LOG_TAG, "linear acceleration values[1]" + event.values[1]);
			Log.d(LOG_TAG, "linear acceleration values[2]" + event.values[2]);
		}
		
		if (Sensor.TYPE_MAGNETIC_FIELD == event.sensor.getType()) {
			/*Log.d(LOG_TAG, "magnetic field values[0]" + event.values[0]);
			Log.d(LOG_TAG, "magnetic field values[1]" + event.values[1]);
			Log.d(LOG_TAG, "magnetic field values[2]" + event.values[2]);*/
			mGeomagnetic = event.values;
		}
		
		if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
			mGravity = event.values;
		}
		
		float[] R = new float[9];
		float[] I = new float[9];
		
		if (mGravity != null && mGeomagnetic != null) {
			boolean result = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			//Log.d(LOG_TAG, "getRotationMatrix() : "+result);	
			
			if (result) {
				Log.d(LOG_TAG, "R[0] = "+R[0]);
				Log.d(LOG_TAG, "R[1] = "+R[1]);
				Log.d(LOG_TAG, "R[2] = "+R[2]);
				Log.d(LOG_TAG, "R[3] = "+R[3]);
				Log.d(LOG_TAG, "R[4] = "+R[4]);
				Log.d(LOG_TAG, "R[5] = "+R[5]);
				Log.d(LOG_TAG, "R[6] = "+R[6]);
				Log.d(LOG_TAG, "R[7] = "+R[7]);
				Log.d(LOG_TAG, "R[8] = "+R[8]);
				
				float[] actual_orientation = new float[3];
			    SensorManager.getOrientation(R, actual_orientation);
			    
			    Log.d(LOG_TAG, "actual_orientation[0] = "+actual_orientation[0]);
			    Log.d(LOG_TAG, "actual_orientation[1] = "+actual_orientation[1]);
			    Log.d(LOG_TAG, "actual_orientation[2] = "+actual_orientation[2]);
			}
			
		}
		
	}
}