package de.htw;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import de.htw.wifi.WiFiComparator;
import de.htw.wifi.WiFiInterface;
import de.htw.wifi.WiFiService;
import de.htw.wifi.WiFiService.LocalWiFiBinder;

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
	
	private List<RemoteBluetoothDevice> mCurrentBluetoothDevices;
	private List<ScanResult> mCurrentWiFiDevices;

	private DAO dao;

	private List<Object> mFoundObjectsFromBluetooth;
    
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
			    	mFoundObjectsFromBluetooth.clear();
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
					mFoundObjectsFromWifi.clear();
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
    
    private void setupDB() {
    	Object o1 = dao.createObject("Trinkflasche");
    	Object o2 = dao.createObject("Buch");
    	Object o3 = dao.createObject("Uhr");
    	Object o4 = dao.createObject("Kugelschreiber");
    	Bluetooth b1 = dao.createBluetooth("INKAMACBOOK", "00:26:08:CB:F4:43");
    	WiFi w1 = dao.createWifi("INKAMACBOOK", "00:26:bb:0e:95:83");
    	dao.createObj_Bt_Relation(o1.getId(), b1.getId());
    	dao.createObj_Bt_Relation(o2.getId(), b1.getId());
    	dao.createObj_Wifi_Relation(o3.getId(), w1.getId());
    	dao.createObj_Bt_Relation(o4.getId(), b1.getId());
    	dao.createObj_Wifi_Relation(o4.getId(), w1.getId());
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
		
		/*StringBuffer buf = new StringBuffer();
		for (RemoteBluetoothDevice device : devices) {
    		Log.d(LOG_TAG, "Address: "+ device.getAddress() + " | RSSI: " + device.getRSSI() + " | Name: " + device.getName());
    		//if (device.getAddress().equalsIgnoreCase("44:2A:60:DA:61:44")) {
    			buf.append(device.getName() + " | " + device.getAddress() + " |Ê" + device.getRSSI()+ "\n");    			
    		//}
    	}*/
		
		mCurrentBluetoothDevices = devices;
		//mBluetoothQuerries.setText(buf.toString());
		
		
		mFoundObjectsFromBluetooth = collectObjectsFromBluetooth(mCurrentBluetoothDevices);
		//printFoundObjects(mFoundObjectsFromBluetooth, "Bluetooth");
		
		printObjects(conjuntObjects(), "Bluetooth");
	}
	
	private void printFoundObjects(List<Object> objects, String source) {
		Log.d(LOG_TAG, "Found Objects after "+source+" scan: "+ objects.size());
        
        for (Object o: objects) {
        	Log.d(LOG_TAG, "ID: " + o.getId() + ", Name: " + o.getObjectName());
        }
	}

	/**
	 * Collect object given a list of remote devices.
	 * @param remoteBluetoothDevices
	 * @return
	 */
	private List<Object> collectObjectsFromBluetooth(List<RemoteBluetoothDevice> remoteBluetoothDevices) {
		List<Object> objects = new ArrayList<Object>();
		
		for (RemoteBluetoothDevice bt : remoteBluetoothDevices) {
			List<Object> found_objects = dao.getObjectsWithBluetooth(dao.getBluetoothByAddress(bt.getAddress()));
			objects.addAll(found_objects);
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
		
		/*
		StringBuffer buf = new StringBuffer();
		for (ScanResult result : results) {
			Log.d(LOG_TAG, "BSSID: " + result.BSSID + " |ÊSignal-Strength: "+ result.level + " |ÊSSID: " + result.SSID);
			buf.append(result.SSID + "Ê|Ê" + result.BSSID + " |Ê" + result.level + "\n");
		}
		*/
		
		mCurrentWiFiDevices = results;
		mFoundObjectsFromWifi = collectObjectsFromWifi(mCurrentWiFiDevices);
		//printFoundObjects(mFoundObjectsFromWifi, "Wifi");
		
		printObjects(conjuntObjects(), "WiFi");
	}
	
	private void printObjects(Set<Object> conjuntObjects, String source) {
		Log.d(LOG_TAG, "Found Objects after scan update ("+source+") : "+ conjuntObjects.size());
		for (Object o : conjuntObjects) {
			Log.d(LOG_TAG, "ID: " + o.getId() + ", Name: " + o.getObjectName());
		}
		
	}

	private Set<Object> conjuntObjects() {
		Log.d(LOG_TAG, "call conjuntObjects()");
		Set<Object> set = new HashSet<Object>();
		for (Object o : mFoundObjectsFromBluetooth) {
			set.add(o);
		}
		for (Object o : mFoundObjectsFromWifi) {
			set.add(o);
		}
		
		return set;
	}
}