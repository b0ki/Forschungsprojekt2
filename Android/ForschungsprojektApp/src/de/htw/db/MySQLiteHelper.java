package de.htw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String LOG_TAG = "MySQLiteHelper";
	
	// Allgemeine Datenbank Attribute
	private static final String DATABASE_NAME = "objects.db";
	private static final int DATABASE_VERSION = 3;
	
	// Tabellen Infos
	public static final String TABLE_OBJECTS = "objects";			// Name der Tabelle "object"
	public static final String COLUMN_ID = "_id";					// Spalte ID
	public static final String COLUMN_OBJECT_NAME = "object_name";	// Spalte Objektname
	
	public static final String TABLE_BLUETOOTH = "bluetooth_networks";		// Name der Tabelle Bluetooth
	public static final String COLUMN_BLUETOOTH_NAME = "bluetooth_name"; // Spalte mit Name des Bluetoothgeräts
	public static final String COLUMN_BLUETOOTH_ADDRESS = "bluetooth_address";	// Spalte mit Adresse des Bluetoothgeräts
	
	public static final String TABLE_WIFI = "wifi_networks";
	public static final String COLUMN_WIFI_SSID = "wifi_ssid";
	public static final String COLUMN_WIFI_BSSID = "wifi_bssid";
	
	private static final String CREATE_OBJECT_TABLE = "create table "
			+ TABLE_OBJECTS + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_OBJECT_NAME
			+ " text not null);";
	
	private static final String CREATE_BLUETOOTH_TABLE = "create table "
			+ TABLE_BLUETOOTH + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_BLUETOOTH_NAME
			+ " text not null, " + COLUMN_BLUETOOTH_ADDRESS
			+ " text not null);";
	
	private static final String CREATE_WIFI_TABLE = "create table "
			+ TABLE_WIFI + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_WIFI_SSID
			+ " text not null, " + COLUMN_WIFI_BSSID
			+ " text not null);";
	
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public MySQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_OBJECT_TABLE);
		db.execSQL(CREATE_BLUETOOTH_TABLE);
		db.execSQL(CREATE_WIFI_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJECTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUETOOTH);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIFI);
		
		onCreate(db);	// Erstelle neue Datenbank
	}

}
