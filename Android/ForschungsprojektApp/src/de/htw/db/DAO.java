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
	
	public DAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
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
}
