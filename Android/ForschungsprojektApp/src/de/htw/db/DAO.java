package de.htw.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DAO{

	private static final String LOG_TAG = "ObjectDAO";
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allObjectColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_OBJECT_NAME};
	private String[] allBluetoothColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_BLUETOOTH_NAME, MySQLiteHelper.COLUMN_BLUETOOTH_ADDRESS};
	private String[] allWifiColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_WIFI_SSID, MySQLiteHelper.COLUMN_WIFI_BSSID};
	private String[] allObj_BtColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_OBJ_FK, MySQLiteHelper.COLUMN_BT_FK};
	private String[] allObj_WifiColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_OBJ_FK, MySQLiteHelper.COLUMN_WIFI_FK};
	
	public DAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Obj_Wifi_Relation createObj_Wifi_Relation(long obj_fk, long wifi_fk) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_OBJ_FK, obj_fk);
		values.put(MySQLiteHelper.COLUMN_WIFI_FK, wifi_fk);
		long insertId = database.insert(MySQLiteHelper.TABLE_OBJ_WIFI, null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJ_WIFI, allObj_WifiColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Obj_Wifi_Relation obj_wifi = cursorToObj_Wifi(cursor);
		cursor.close();
		return obj_wifi;
	}
	
	private Obj_Wifi_Relation cursorToObj_Wifi(Cursor cursor) {
		Obj_Wifi_Relation obj_wifi = new Obj_Wifi_Relation();
		obj_wifi.setId(cursor.getLong(0));
		obj_wifi.setObj_fk(cursor.getLong(1));
		obj_wifi.setWifi_fk(cursor.getLong(2));
		return obj_wifi;
	}
	
	public void deleteObj_Wifi(Obj_Wifi_Relation obj_wifi) {
		long id = obj_wifi.getId();
		Log.d(LOG_TAG, "Obj_Wifi_Rel deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_OBJ_WIFI, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Obj_Wifi_Relation> getAllObj_Wifis() {
		List<Obj_Wifi_Relation> obj_wifis = new ArrayList<Obj_Wifi_Relation>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJ_WIFI, allObj_WifiColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Obj_Wifi_Relation obj_wifi = cursorToObj_Wifi(cursor);
			obj_wifis.add(obj_wifi);
			cursor.moveToNext();
		}
		cursor.close();
		return obj_wifis;
	}

	public Obj_Bt_Relation createObj_Bt_Relation(long obj_fk, long bt_fk) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_OBJ_FK, obj_fk);
		values.put(MySQLiteHelper.COLUMN_BT_FK, bt_fk);
		long insertId = database.insert(MySQLiteHelper.TABLE_OBJ_BT, null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJ_BT, allObj_BtColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Obj_Bt_Relation obj_bt = cursorToObj_Bt(cursor);
		cursor.close();
		return obj_bt;
	}
	
	private Obj_Bt_Relation cursorToObj_Bt(Cursor cursor) {
		Obj_Bt_Relation obj_bt = new Obj_Bt_Relation();
		obj_bt.setId(cursor.getLong(0));
		obj_bt.setObj_fk(cursor.getLong(1));
		obj_bt.setBt_fk(cursor.getLong(2));
		return obj_bt;
	}
	
	public void deleteObj_Bt(Obj_Bt_Relation obj_bt) {
		long id = obj_bt.getId();
		Log.d(LOG_TAG, "Obj_Bt_Rel deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_OBJ_BT, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Obj_Bt_Relation> getAllObj_Bts() {
		List<Obj_Bt_Relation> obj_bts = new ArrayList<Obj_Bt_Relation>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJ_BT, allObj_BtColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Obj_Bt_Relation obj_bt = cursorToObj_Bt(cursor);
			obj_bts.add(obj_bt);
			cursor.moveToNext();
		}
		cursor.close();
		return obj_bts;
	}

	public WiFi createWifi(String ssid, String bssid) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_WIFI_SSID, ssid);
		values.put(MySQLiteHelper.COLUMN_WIFI_BSSID, bssid);
		long insertId = database.insert(MySQLiteHelper.TABLE_WIFI, null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WIFI, allWifiColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		WiFi wifi = cursorToWifi(cursor);
		cursor.close();
		return wifi;
	}
	
	private WiFi cursorToWifi(Cursor cursor) {
		WiFi wifi = new WiFi();
		wifi.setId(cursor.getLong(0));
		wifi.setSsid(cursor.getString(1));
		wifi.setBssid(cursor.getString(2));
		return wifi;
	}
	
	public void deleteWifi(WiFi wf) {
		long id = wf.getId();
		Log.d(LOG_TAG, "WiFi deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_WIFI, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<WiFi> getAllWifis() {
		List<WiFi> wifis = new ArrayList<WiFi>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WIFI, allWifiColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			WiFi wf = cursorToWifi(cursor);
			wifis.add(wf);
			cursor.moveToNext();
		}
		cursor.close();
		return wifis;
	}

	public Bluetooth createBluetooth(String name, String address) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_BLUETOOTH_NAME, name);
		values.put(MySQLiteHelper.COLUMN_BLUETOOTH_ADDRESS, address);
		long insertId = database.insert(MySQLiteHelper.TABLE_BLUETOOTH, null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_BLUETOOTH, allBluetoothColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Bluetooth bt= cursorToBluetooth(cursor);
		cursor.close();
		return bt;
	}
	
	private Bluetooth cursorToBluetooth(Cursor cursor) {
		Bluetooth bt = new Bluetooth();
		bt.setId(cursor.getLong(0));
		bt.setName(cursor.getString(1));
		bt.setAddress(cursor.getString(2));
		return bt;
	}
	
	public void deleteBluetooth(Bluetooth bt) {
		long id = bt.getId();
		Log.d(LOG_TAG, "Bluetooth deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_BLUETOOTH, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Bluetooth> getAllBluetooths() {
		List<Bluetooth> bluetooths = new ArrayList<Bluetooth>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_BLUETOOTH, allBluetoothColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Bluetooth bt = cursorToBluetooth(cursor);
			bluetooths.add(bt);
			cursor.moveToNext();
		}
		cursor.close();
		return bluetooths;
	}

	public Object createObject(String object_name) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_OBJECT_NAME, object_name);
		long insertId = database.insert(MySQLiteHelper.TABLE_OBJECTS, null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJECTS, allObjectColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Object object= cursorToObject(cursor);
		cursor.close();
		return object;
	}

	private Object cursorToObject(Cursor cursor) {
		Object object = new Object();
		object.setId(cursor.getLong(0));
		object.setObjectName(cursor.getString(1));
		return object;
	}
	
	public void deleteObject(Object object) {
		long id = object.getId();
		Log.d(LOG_TAG, "Object deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_OBJECTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Object> getAllObjects() {
		List<Object> objects = new ArrayList<Object>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJECTS, allObjectColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Object object = cursorToObject(cursor);
			objects.add(object);
			cursor.moveToNext();
		}
		cursor.close();
		return objects;
	}
	
	/**
	 * Get a Bluetooth entry from database by its given address.
	 * @param address 
	 * @return Can be null.
	 */
	public Bluetooth getBluetoothByAddress(String address) {
		String WHERE = MySQLiteHelper.COLUMN_BLUETOOTH_ADDRESS + " = " + "'"+address+"'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_BLUETOOTH, allBluetoothColumns, WHERE, null, null, null, null);
		cursor.moveToFirst();
		Bluetooth bt = null;
		if (!cursor.isAfterLast()) {
			bt = cursorToBluetooth(cursor);			
		}
		cursor.close();
		return bt;
	}
	
	
	
	/**
	 * Get all objects which have a relation to the given bluetooth device.
	 * @param bt
	 * @return
	 */
	public List<Object> getObjectsWithBluetooth(Bluetooth bt) {
		if (bt == null) {
			return new ArrayList<Object>();
		}
		
		String where_statement = MySQLiteHelper.COLUMN_BT_FK + " = " + bt.getId();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJ_BT, allObj_BtColumns, where_statement, null, null, null, null);
		cursor.moveToFirst();
		
		// Hole alle Object IDS aus Tabelle die mit der Bluetooth Adresse verbunden sind
		List<Long> obj_ids = new ArrayList<Long>();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(1);
			obj_ids.add(new Long(id));
			cursor.moveToNext();
		}
		cursor.close();
		
		// Now we can get all Objects
		List<Object> objects = new ArrayList<Object>();
		for (Long l : obj_ids) {
			objects.add(getObjectByID(l.longValue()));
		}
		
		return objects;
	}
	
	public Object getObjectByID(long id) {
		String where_statement = MySQLiteHelper.COLUMN_ID + " = " + id;
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJECTS, allObjectColumns, where_statement, null, null, null, null);
		cursor.moveToFirst();
		Object object = cursorToObject(cursor);
		cursor.close();
		return object;
	}

	/**
	 * Get a Wifi entry by a given bssid.
	 * @param bSSID
	 * @return
	 */
	public WiFi getWifiByBSSID(String bssid) {
		String WHERE = MySQLiteHelper.COLUMN_WIFI_BSSID + " = " + "'"+bssid+"'";
		Cursor cursor = database.query(MySQLiteHelper.TABLE_WIFI, allWifiColumns, WHERE, null, null, null, null);
		cursor.moveToFirst();
		WiFi wf = null;
		if (!cursor.isAfterLast()) {
			wf = cursorToWifi(cursor);			
		}
		cursor.close();
		return wf;
	}

	public List<Object> getObjectsWithWifi(WiFi wifi) {
		if (wifi == null) {
			return new ArrayList<Object>();
		}
		
		String where_statement = MySQLiteHelper.COLUMN_WIFI_FK + " = " + wifi.getId();
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJ_WIFI, allObj_WifiColumns, where_statement, null, null, null, null);
		cursor.moveToFirst();
		
		// Hole alle Object IDS aus Tabelle die mit der Wifi Adresse verbunden sind
		List<Long> obj_ids = new ArrayList<Long>();
		while (!cursor.isAfterLast()) {
			long id = cursor.getLong(1);
			obj_ids.add(new Long(id));
			cursor.moveToNext();
		}
		cursor.close();
		
		// Now we can get all Objects
		List<Object> objects = new ArrayList<Object>();
		for (Long l : obj_ids) {
			objects.add(getObjectByID(l.longValue()));
		}
		
		return objects;
	}
}
