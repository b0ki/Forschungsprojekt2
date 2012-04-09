package de.htw;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.impl.conn.Wire;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import de.htw.bluetooth.BluetoothInterface;
import de.htw.bluetooth.BluetoothService;
import de.htw.bluetooth.BluetoothService.LocalBluetoothBinder;
import de.htw.bluetooth.RemoteBluetoothDevice;
import de.htw.db.Bluetooth;
import de.htw.db.DAO;
import de.htw.db.Obj_Bt_Relation;
import de.htw.db.Obj_Wifi_Relation;
import de.htw.db.Object;
import de.htw.db.WiFi;
import de.htw.light.LightInterface;
import de.htw.light.LightService;
import de.htw.light.LightService.LocalLightBinder;
import de.htw.light.LightTracker;
import de.htw.wifi.WiFiComparator;
import de.htw.wifi.WiFiInterface;
import de.htw.wifi.WiFiService;
import de.htw.wifi.WiFiService.LocalWiFiBinder;

public class ForschungsprojektAppActivity extends Activity implements BluetoothInterface, WiFiInterface, SensorEventListener, LightInterface {

	private static final String LOG_TAG = "ForschungsprojektAppActivity";
	
	private ToggleButton mBluetoothToggle;
	
	// Service State
	private LightService mLightService;
	private BluetoothService mBluetoothService;
	private WiFiService mWiFiService;
    private boolean isBluetoothServiceBound;
    private boolean isWiFiServiceBound;

    // Bluetooth
	private BluetoothAdapter mBluetoothAdapter;
	private int REQUEST_ENABLE_BT = 0;

	private TextView mBluetoothQuerries;

	private ToggleButton mWiFiScan;
	
	private List<RemoteBluetoothDevice> mCurrentBluetoothDevices;
	private List<ScanResult> mCurrentWiFiDevices;

	private DAO dao;

	private List<Object> mFoundObjectsFromBluetooth;

	private ToggleButton mLightSensorButton;

	private SensorManager mSensorManager;

	private Sensor mLight;

	private Time mCurrentTime;

	private TextView mTextObjectList;
	
	private boolean isLightServiceBound;
	
	public final static String SETUP_DB = "Setup DB";
	public final static String DELETE_DB = "Delete DB";
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mTextObjectList = (TextView) findViewById(R.id.text_object_list);
        
        mCurrentTime = new Time();
        //mCurrentWirelessObjects = new HashSet<Object>();
        mCurrentWirelessObjects = new ArrayList<Object>();
        mCurrentLightObjects = new ArrayList<Object>();
        
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
			    	mFoundObjectsFromBluetooth.clear();
			    	conjuntWirelessObjects();
			    	printCurrentObjects();
				}
				
			}

			
		});
        
        setupBluetooth();
        
        
        mWiFiScan = (ToggleButton) findViewById(R.id.button_wifi_scan);
        mWiFiScan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
				if (mWiFiScan.isChecked()) {
					Intent service = new Intent(ForschungsprojektAppActivity.this, WiFiService.class);
					bindService(service, mWiFiConnection, Context.BIND_AUTO_CREATE);
					isWiFiServiceBound = true;
				} else {
					unbindService(mWiFiConnection);
					isWiFiServiceBound = false;
					mFoundObjectsFromWifi.clear();
			    	conjuntWirelessObjects();
			    	printCurrentObjects();
				}
			}
		});
        
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        
        mLightSensorButton = (ToggleButton) findViewById(R.id.button_light_sensor);
        mLightSensorButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mLightSensorButton.isChecked()) {
					// Der Lichtsensor arbeitet nur wenn ein Wireless Service läuft.
					if (isBluetoothServiceBound || isWiFiServiceBound) {
						//mSensorManager.registerListener(ForschungsprojektAppActivity.this, mLight, SensorManager.SENSOR_DELAY_UI);
						Intent service = new Intent(ForschungsprojektAppActivity.this, LightService.class);
						Log.d(LOG_TAG, "try to bind Service");
						bindService(service, mLightConnection, Context.BIND_AUTO_CREATE);
						isLightServiceBound = true;
					} else {
						mLightSensorButton.setChecked(false);
						Toast.makeText(ForschungsprojektAppActivity.this, "Need Wireless Service", Toast.LENGTH_SHORT).show();
					}
				} else {
					//mSensorManager.unregisterListener(ForschungsprojektAppActivity.this);
					unbindService(mLightConnection);
					isLightServiceBound = false;
					conjuntWirelessObjects();
					printCurrentObjects();
				}
				
			}
		});
        
        mCurrentBluetoothDevices = new ArrayList<RemoteBluetoothDevice>();
        mCurrentWiFiDevices = new ArrayList<ScanResult>();
        
        dao = new DAO(this);
        dao.open();
        
        //setupDB();
        //destroyDB();
        List<Object> objects = dao.getAllObjects();
        
        //dao.deleteObject(objects.get(0));
        //objects = dao.getAllObjects();
        
        Log.d(LOG_TAG, "Objects in Database: "+objects.size());
        
        for (Object o : objects) {
        	Log.d(LOG_TAG, "ID: " + o.getId() + ", Name: " + o.getObjectName());
        }
        
        List<Bluetooth> bluetooths = dao.getAllBluetooths();
        
        Log.d(LOG_TAG, "Bluetooth Devices in Database: "+ bluetooths.size());
        
        for (Bluetooth bt : bluetooths) {
        	Log.d(LOG_TAG, "ID: " + bt.getId() + ", Name: " + bt.getName() + ", Address: " + bt.getAddress());
        }
        
        List<WiFi> wifis = dao.getAllWifis();
        
        Log.d(LOG_TAG, "WiFi Devices in Database: "+ wifis.size());
        
        for (WiFi wf : wifis) {
        	Log.d(LOG_TAG, wf.getSsid() + " " + wf.getBssid());
        }
        
        List<Obj_Bt_Relation> obj_bts = dao.getAllObj_Bts();
        
        Log.d(LOG_TAG, "Obj_BT Relationen in Database: "+ obj_bts.size());
        
        for (Obj_Bt_Relation obj_bt : obj_bts) {
        	Log.d(LOG_TAG, "ID: " + obj_bt.getId() + ", Obj FK: "+obj_bt.getObj_fk() + ", BT FK: " + obj_bt.getBt_fk());
        }
        
        List<Obj_Wifi_Relation> obj_wifis = dao.getAllObj_Wifis();
        
        Log.d(LOG_TAG, "Obj_Wifi Relationen in Database: "+ obj_wifis.size());
        
        for (Obj_Wifi_Relation obj_wifi : obj_wifis) {
        	Log.d(LOG_TAG, "ID: " + obj_wifi.getId() + ", Obj FK: "+obj_wifi.getObj_fk() + ", WiFi FK: " + obj_wifi.getWifi_fk());
        }
        
        mFoundObjectsFromBluetooth = new ArrayList<Object>();
        mFoundObjectsFromWifi = new ArrayList<Object>();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(SETUP_DB);
    	menu.add(DELETE_DB);
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d(LOG_TAG, "press item: "+item.getTitle());
    	if (item.getTitle().toString().equals(SETUP_DB)) {
    		setupDB();
    	}
    	
    	if (item.getTitle().toString().equals(DELETE_DB)) {
    		destroyDB();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    private void setupDB() {
    	Object o1 = dao.createObject("Trinkflasche", 10); // im sehr dunkel
    	Object o2 = dao.createObject("Buch", 320);		// im hellen
    	Object o3 = dao.createObject("Uhr", 1024);		// am Fenster
    	Object o4 = dao.createObject("Kugelschreiber", 512);
    	Object o5 = dao.createObject("MacBook", 620); // Mein MacBook
    	Bluetooth b1 = dao.createBluetooth("INKAMACBOOK", "00:26:08:CB:F4:43");
    	Bluetooth b2 = dao.createBluetooth("Christians_MacBook", "00:25:00:60:A8:C6");
    	WiFi w1 = dao.createWifi("INKAMACBOOK", "00:26:bb:0e:95:83");
    	dao.createObj_Bt_Relation(o1.getId(), b1.getId());
    	dao.createObj_Bt_Relation(o2.getId(), b1.getId());
    	dao.createObj_Wifi_Relation(o3.getId(), w1.getId());
    	dao.createObj_Bt_Relation(o4.getId(), b1.getId());
    	dao.createObj_Wifi_Relation(o4.getId(), w1.getId());
    	dao.createObj_Bt_Relation(o5.getId(), b2.getId());
    }
    
    private void destroyDB() {
    	 List<Object> objects = dao.getAllObjects();
         
         for (Object o : objects) {
        	dao.deleteObject(o);
         }
         
         List<Bluetooth> bluetooths = dao.getAllBluetooths();
         
         for (Bluetooth bt : bluetooths) {
        	dao.deleteBluetooth(bt);
         }
         
         List<WiFi> wifis = dao.getAllWifis();
         
         for (WiFi wf : wifis) {
        	dao.deleteWifi(wf);
         }
         
         List<Obj_Bt_Relation> obj_bts = dao.getAllObj_Bts();
         
         for (Obj_Bt_Relation obj_bt : obj_bts) {
        	dao.deleteObj_Bt(obj_bt);
         }
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
		//Log.d(LOG_TAG, "Bluetooth Networks: ");
		
		// sortiere Liste
		Collections.sort(devices);
		
		//StringBuffer buf = new StringBuffer();
		for (RemoteBluetoothDevice device : devices) {
    		Log.d(LOG_TAG, "Address: "+ device.getAddress() + " | RSSI: " + device.getRSSI() + " | Name: " + device.getName());
    		//if (device.getAddress().equalsIgnoreCase("44:2A:60:DA:61:44")) {
    			//buf.append(device.getName() + " | " + device.getAddress() + " | " + device.getRSSI()+ "\n");    			
    		//}
    	}
		
		mCurrentBluetoothDevices = devices;
		//mBluetoothQuerries.setText(buf.toString());
		
		
		mFoundObjectsFromBluetooth = collectObjectsFromBluetooth(mCurrentBluetoothDevices);
		//printFoundObjects(mFoundObjectsFromBluetooth, "Bluetooth");
		
		conjuntWirelessObjects();
		
		if (isLightServiceBound) {
			conjunctLightObjects();
		}
		
		printCurrentObjects();
		
		//printObjects(conjuntWirelessObjects(), "Bluetooth");
	}
	
	/*private void printFoundObjects(List<Object> objects, String source) {
		Log.d(LOG_TAG, "Found Objects after "+source+" scan: "+ objects.size());
        
        for (Object o: objects) {
        	Log.d(LOG_TAG, "ID: " + o.getId() + ", Name: " + o.getObjectName());
        }
	}*/

	/**
	 * Collect object given a list of remote devices.
	 * @param remoteBluetoothDevices
	 * @return
	 */
	private List<Object> collectObjectsFromBluetooth(List<RemoteBluetoothDevice> remoteBluetoothDevices) {
		Log.d(LOG_TAG, "collect Objects for Bluetooth...");
		List<Object> objects = new ArrayList<Object>();
		
		for (RemoteBluetoothDevice bt : remoteBluetoothDevices) {
			Log.d(LOG_TAG, "for bluetooth device: "+bt.getAddress()); 
			List<Object> found_objects = dao.getObjectsWithBluetooth(dao.getBluetoothByAddress(bt.getAddress()));
			objects.addAll(found_objects);
		}
		
		Log.d(LOG_TAG, "collected objects:");
		for (Object o : objects) {
			Log.d(LOG_TAG, o.getObjectName());
		}
		
		return objects;
	}
	
	/**
	 * Collect objects given a list of wifi devices.
	 * @param remoteBluetoothDevices
	 * @return
	 */
	private List<Object> collectObjectsFromWifi(List<ScanResult> wifiNetworks) {
		List<Object> objects = new ArrayList<Object>();
		
		for (ScanResult wifi : wifiNetworks) {
			List<Object> found_objects = dao.getObjectsWithWifi(dao.getWifiByBSSID(wifi.BSSID));
			objects.addAll(found_objects);
		}
		
		return objects;
	}
	
	private List<Object> collectObjectsFromLight(int light_value) {
		List<Object> objects = new ArrayList<Object>();
		
		Log.d(LOG_TAG, "get objects with given light from db: " + light_value);
		objects = dao.getObjectsWithLight(light_value);
		
		Log.d(LOG_TAG, "size: "+objects.size());
		
		return objects;
	}
	
	private ServiceConnection mLightConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			Log.d(LOG_TAG, "connect to Light service");
			
			LocalLightBinder binder = (LocalLightBinder) service;
			mLightService = binder.getService();
			mLightService.registerCallback(ForschungsprojektAppActivity.this);
			//mLightService.setSleepInterval(10000);
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};

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

	private List<Object> mFoundObjectsFromWifi;

	private long mLastUpdate;

	private List<Object> mCurrentWirelessObjects;

	private List<Object> mCurrentLightObjects;

	private List<Object> mCurrentObjects;

	private int mLastLightValue;
    
    
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
    	
    	dao.close();
    	
    	super.onDestroy();
    }

	@Override
	public void onScannedWifi(List<ScanResult> results) {
		//Log.d(LOG_TAG, "WiFi Networks:");
		Collections.sort(results, new WiFiComparator());
		
		
		//StringBuffer buf = new StringBuffer();
		for (ScanResult result : results) {
			Log.d(LOG_TAG, "BSSID: " + result.BSSID + " | Signal-Strength: "+ result.level + " | SSID: " + result.SSID);
			//buf.append(result.SSID + " | " + result.BSSID + " | " + result.level + "\n");
		}
		
		
		mCurrentWiFiDevices = results;
		mFoundObjectsFromWifi = collectObjectsFromWifi(mCurrentWiFiDevices);
		//printFoundObjects(mFoundObjectsFromWifi, "Wifi");
		
		conjuntWirelessObjects();
		if (isLightServiceBound) {
			conjunctLightObjects();
		}
		printCurrentObjects();
		
		//printObjects(conjuntWirelessObjects(), "WiFi");
	}

	/**
	 * Wird aufgerufen wenn sich entweder Wifi oder Bluetooth geändert hat
	 * @return
	 */
	private List<Object> conjuntWirelessObjects() {
		Log.d(LOG_TAG, "call conjuntWirelessObjects()");
		//Set<Object> set = new HashSet<Object>();
		List<Object> objects = new ArrayList<Object>();
		
		// Füge die letzten Bluetooth gefundenen Objekte hinzu
		for (Object o : mFoundObjectsFromBluetooth) {
			//set.add(o);
			if (!objects.contains(o)) {
				objects.add(o);
			}
		}
		
		// Füge die letzten WiFi gefundenen Objekte hinzu
		for (Object o : mFoundObjectsFromWifi) {
			//set.add(o);
			if (!objects.contains(o)) {
				objects.add(o);
			}
		}

		mCurrentWirelessObjects = objects;
		mCurrentObjects = objects;
		/*
		if (mCurrentLightObjects.size() > 0) {
			objects.clear();
			// Nur die Schnittmenge nehmen
			for (Object o : mCurrentLightObjects) {
				if (mCurrentWirelessObjects.contains(o)) {
					objects.add(o);
				}
			}
			
			mCurrentObjects = objects;
		}*/
		
		
		
		return objects;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Sensor.TYPE_LIGHT == event.sensor.getType()) {
			Log.d(LOG_TAG, "light value: " + event.values[0]);
			
	        mCurrentTime.setToNow();
	        int daytime = mCurrentTime.hour;
	        Log.d(LOG_TAG, "hour: "+mCurrentTime.hour);
	        Log.d(LOG_TAG, "estimated light value at Window: " + LightTracker.getEstimatedLightValueWindowForDayTime(daytime));
	        Log.d(LOG_TAG, "estimated light value at Wall: " + LightTracker.getEstimatedLightValueWallForDayTime(daytime));
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(ForschungsprojektAppActivity.this);
		super.onPause();
	}

	@Override
	public void onScannedLight(int light_value) {
		Log.d(LOG_TAG, "light value (lux): " + light_value);
		mLastLightValue = light_value;
		
		
		// Hole Objekte anhand des letzten Lichtwertes
		mCurrentLightObjects = collectObjectsFromLight(mLastLightValue);
		
		//Set<Object> object_set = new HashSet<Object>();
		
		/*for (Object o : objects) {
			object_set.add(o);
		}*/
		
		//mCurrentLightObjects = objects;
		Log.d(LOG_TAG, "size light objects: "+mCurrentLightObjects.size());
		//conjuntObjects(object_set);
		conjunctLightObjects();
		printCurrentObjects();
		
	}
	
	/**
	 * Wird aufgerufen wenn sich der Lichtwert geändert hat.
	 */
	private void conjunctLightObjects() {
		//Set<Object> conjunct_objects = new HashSet<Object>();
		
		List<Object> objects = new ArrayList<Object>();
		
		if (mCurrentLightObjects.size() == 0) {
			mCurrentObjects = mCurrentLightObjects;
		} else {
			// Nur die Schnittmenge nehmen
			for (Object o : mCurrentLightObjects) {
				if (mCurrentWirelessObjects.contains(o)) {
					objects.add(o);
				}
			}
			
			mCurrentObjects = objects;			
		}
	}

	private void printCurrentObjects() {
		
		if (mCurrentObjects.size() == 0) {
			Log.d(LOG_TAG, "No Objects!");
			mTextObjectList.setText("No Objects!");
			return;
		}
		
		StringBuffer buf = new StringBuffer();
		for (Object o : mCurrentObjects) {
			Log.d(LOG_TAG, "ID: " + o.getId() + ", Name: " + o.getObjectName());
			buf.append(o.getId() + " " + o.getObjectName()+"\n");
		}
		buf.append("Last update: " + (System.currentTimeMillis() - mLastUpdate)/1000 + " sec");
		
		mLastUpdate = System.currentTimeMillis();
		mTextObjectList.setText(buf.toString());
		
	}
}