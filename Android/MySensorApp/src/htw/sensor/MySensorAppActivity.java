package htw.sensor;

import android.app.Activity;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
	
	private TextView mTextRotationMatrix;

	private TextView mTextRotationVector;

	private Sensor mRotationVector;

	private float[] mRotation;

	private Matrix mRotationMatrix;

	private TextView mTextBetrag;

	private float[] mLinearAccelerationValues;

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
        
        mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        
        mLinearAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        
        
        /*
        float[] input = new float[3];
        float[][] rotationMatrix = new float[3][3];
        
        input[0] = 1;
        input[1] = 2;
        input[2] = 3;
        
        rotationMatrix[0][0] = 2;
        rotationMatrix[0][1] = 3;
        rotationMatrix[0][2] = 4;
        
        rotationMatrix[1][0] = 3;
        rotationMatrix[1][1] = 4;
        rotationMatrix[1][2] = 5;
        
        rotationMatrix[2][0] = 4;
        rotationMatrix[2][1] = 5;
        rotationMatrix[2][2] = 6;
        
        float[] result = matrixMultiply(input, rotationMatrix);
        Log.d(LOG_TAG, "Matrix multiplication:");
        Log.d(LOG_TAG, result[0] +" "+ result[1] + " " + result[2]);
        */
        
        mTextRotationMatrix = (TextView) findViewById(R.id.text_rotation_matrix);
        mTextRotationVector = (TextView) findViewById(R.id.text_rotation_vector);
        mTextBetrag = (TextView) findViewById(R.id.text_betrag);
        
        /*float value = 0.924f;
        float rounded = Math.round(value*100)/100f;
        mLightInfo.setText("gerundet: "+roundNDigitsAfterComma(value, 0));
        //mLightInfo.setText("gerundet: "+round1DigitAfterComma(value));*/
        Matrix m = new Matrix();
        Matrix n = new Matrix();
        float[] values = new float[9];
        
        float[] point = new float[9];
        point[0] = 1;
        point[1] = 2;
        point[2] = 3;
        point[3] = 4;
        point[4] = 5;
        point[5] = 6;
        point[6] = 7;
        point[7] = 8;
        point[8] = 9;
        m.setValues(point);
        n.setValues(point);
        
        float[] vec = new float[3];
        vec[0] = 2;
        vec[1] = 3;
        vec[2] = 4;
        
        float[] dest = new float[3];
        
        Matrix l = new Matrix();
        l.setConcat(m, n);
        l.getValues(values);
        
        //Matrix invertMatrix = new Matrix(m);
        //float[] inverted = new float[9];
        //Log.d(LOG_TAG, "invert matrix: " + m.invert(m));
        //m.getValues(values);
        printRotationMatrix(values);
        //printVector(values, "");
        
        mRotationMatrix = new Matrix();
    }
    
    /**
     * Rundet eine Zahl auf n Stellen nach dem Komma.
     * @param value Zu rundende Zahl.
     * @param n Stelle bis zu der nach dem Komma gerundet werden soll. n > 0
     * @return Gerundete Zahl.
     */
    private float roundNDigitsAfterComma(float value, int n) {
    	if (n <= 0) {
    		return Float.MIN_NORMAL;
    	}
    	
    	float exp = (float) Math.pow(10, n);

    	return Math.round(value*exp)/exp;
    	
    }
    
    private float[] roundNDigitsAfterComma(float[] values, int size, int n) {
    	float result[] = new float[size];
    	for (int i = 0; i < size; i++) {
    		result[i] = roundNDigitsAfterComma(values[i], n);
    	}
    	
    	return result;
    }
    
    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        
        mSensorManager.registerListener(this, mRotationVector, SensorManager.SENSOR_DELAY_NORMAL);
        
        mSensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        
        mSensorManager.registerListener(this, mLinearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
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
			mTextRotationMatrix.setText(new Float(event.values[0]).toString());
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
			
			//Log.d(LOG_TAG, "Magnetfeldwerte (x,y,z): ");
			//Log.d(LOG_TAG, event.values[0] + " " + event.values[1] + " " + event.values[2]);
			mGeomagnetic = event.values.clone();
		}
		
		if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
			mGravity = event.values.clone();
		}
		
		if (Sensor.TYPE_ROTATION_VECTOR == event.sensor.getType()) {
			mRotation = event.values.clone();
		}
		
		if (Sensor.TYPE_LINEAR_ACCELERATION == event.sensor.getType()) {
			mLinearAccelerationValues = event.values.clone();
		}
		
		float[] R = new float[9];
		float[] I = new float[9];
		//float[] rotation = new float[16];
		//float[] orientVals = new float[3];
		//final float pi = (float) Math.PI;
		//final float rad2deg = 180/pi;
		
		/*if (mGravity != null && mGeomagnetic != null) {
			boolean result = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			
			if (result) {
				// rundet Matrix auf n Stellen nach dem Komma
				R = roundNDigitsAfterComma(R, 9, 1);
				printRotationMatrix(R);
				
				// Multipliziere Rotationsmatrix mit Gravity Vektor
				float[] rotatedVector = matrixMultiply(mGeomagnetic, transformRotationMatrix(R));
				printVector(rotatedVector, "rotated gravity vector");
			}
			
		}*/
		/*
		if (mRotation != null && mGeomagnetic != null) {
			SensorManager.getRotationMatrixFromVector(R, mRotation);
			
			Matrix rotationMatrix = new Matrix();
			rotationMatrix.setValues(R);
			//Log.d(LOG_TAG, "is invertable? "+rotationMatrix.invert(rotationMatrix));
			rotationMatrix.invert(rotationMatrix);
			rotationMatrix.getValues(R);
			R = roundNDigitsAfterComma(R, 9, 1);
			printRotationMatrix(R);
			
			// Multipliziere Rotationsmatrix mit Gravity Vektor
			float[] rotatedVector = matrixMultiply(mGeomagnetic, transformRotationMatrix(R));
			//printVector(roundNDigitsAfterComma(mGravity, 3, 1), "rotated gravity vector");
			printVector(roundNDigitsAfterComma(rotatedVector, 3, 1), "rotated geomagnetic vector");
			
			double betrag = Math.sqrt(Math.pow(rotatedVector[0],2) + Math.pow(rotatedVector[1],2) + Math.pow(rotatedVector[2],2));
			Log.d(LOG_TAG, "Betrag: "+betrag);
			mTextBetrag.setText(new Double(betrag).toString());
		}*/
		
		if (mRotation != null && mLinearAccelerationValues != null) {
			mLinearAccelerationValues = roundNDigitsAfterComma(mLinearAccelerationValues, 3, 1);
			//printVector(mLinearAccelerationValues, "");
			
			SensorManager.getRotationMatrixFromVector(R, mRotation);
			
			Matrix rotationMatrix = new Matrix();
			rotationMatrix.setValues(R);
			//Log.d(LOG_TAG, "is invertable? "+rotationMatrix.invert(rotationMatrix));
			rotationMatrix.invert(rotationMatrix);
			rotationMatrix.getValues(R);
			R = roundNDigitsAfterComma(R, 9, 1);
			printRotationMatrix(R);
			
			// Multipliziere Rotationsmatrix mit Gravity Vektor
			float[] rotatedVector = matrixMultiply(mLinearAccelerationValues, transformRotationMatrix(R));
			//printVector(roundNDigitsAfterComma(mGravity, 3, 1), "rotated gravity vector");
			printVector(roundNDigitsAfterComma(rotatedVector, 3, 1), "rotated linear accelaration vector");
			
			double betrag = Math.sqrt(Math.pow(rotatedVector[0],2) + Math.pow(rotatedVector[1],2) + Math.pow(rotatedVector[2],2));
			Log.d(LOG_TAG, "Betrag: "+betrag);
			mTextBetrag.setText(new Double(betrag).toString());
		}
		
	}
	
	/** Print a Vektor with 3 elements
	 * 
	 * @param input
	 */
	private void printVector(float[] input, String name) {
		StringBuffer buf = new StringBuffer();
		buf.append("( "+ input[0] + " " + input[1] + " " + input[2] + ")");
		
		Log.d(LOG_TAG, name);
		Log.d(LOG_TAG, "( "+ input[0] + " " + input[1] + " " + input[2] + ")");
		mTextRotationVector.setText(buf.toString());
	}
	
	/**
	 * Multipliziert eine 1x3 Matrix mit einer 3x3 Matrix..
	 * @param input [spalte]
	 * @param rotationMatrix float[zeile][spalte]
	 * @return
	 */
	private float[] matrixMultiply(float[] input, float[][] rotationMatrix) {
		float[] result = new float[3];
		for (int spalte = 0; spalte < 3; spalte++) {
			int sum = 0;
			for (int zeile = 0; zeile < 3; zeile++) {
				sum += input[zeile] * rotationMatrix[zeile][spalte];
			}
			
			result[spalte] = sum;
		}
		
		return result;
	}
	
	private float[][] transformRotationMatrix(float[] originRotationMatrix) {
		float[][] result = new float[3][3];
		
		result[0][0] = originRotationMatrix[0];
		result[0][1] = originRotationMatrix[1];
		result[0][2] = originRotationMatrix[2];
		result[1][0] = originRotationMatrix[3];
		result[1][1] = originRotationMatrix[4];
		result[1][2] = originRotationMatrix[5];
		result[2][0] = originRotationMatrix[6];
		result[2][1] = originRotationMatrix[7];
		result[2][2] = originRotationMatrix[8];
		
		return result;
	}
	
	private void printRotationMatrix(float[] R) {
		StringBuffer buf = new StringBuffer();
		buf.append(R[0] + " " + R[1] + " " + R[2] + "\n");
		buf.append(R[3] + " " + R[4] + " " + R[5] + "\n");
		buf.append(R[6] + " " + R[7] + " " + R[8]);
		
		mTextRotationMatrix.setText(buf.toString());
		
		/*
		Log.d(LOG_TAG, "Rotation Matrix: ");
		Log.d(LOG_TAG, "| "+ R[0] + " " + R[1] + " " + R[2] + "Ê|");
		Log.d(LOG_TAG, "| "+ R[3] + " " + R[4] + " " + R[5] + "Ê|");
		Log.d(LOG_TAG, "| "+ R[6] + " " + R[7] + " " + R[8] + "Ê|");
		*/
	}
}