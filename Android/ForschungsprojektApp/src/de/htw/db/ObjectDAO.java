package de.htw.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ObjectDAO{

	private static final String LOG_TAG = "ObjectDAO";
	
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_OBJECT_NAME};
	
	public ObjectDAO(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Object createObject(String object_name) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_OBJECT_NAME, object_name);
		long insertId = database.insert(MySQLiteHelper.TABLE_OBJECTS, null, values);
		
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJECTS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
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
		Cursor cursor = database.query(MySQLiteHelper.TABLE_OBJECTS, allColumns, null, null, null, null, null);
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
