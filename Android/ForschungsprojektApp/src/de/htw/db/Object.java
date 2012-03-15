package de.htw.db;

import android.util.Log;
import de.htw.bluetooth.RemoteBluetoothDevice;

/**
 * This class is our model and contains the data we will save in the database.
 * @author christian
 *
 */
public class Object {
	
	private static final String LOG_TAG = "Object";

	private long id;
	private String object_name;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getObjectName() {
		return object_name;
	}
	
	public void setObjectName(String object_name) {
		this.object_name = object_name;
	}
	
	@Override
	public String toString() {
		return object_name;
	}
	
	@Override
	public boolean equals(java.lang.Object o) {
		Log.d(LOG_TAG, "call equals");
		if (this == o) {
			return true;
		}
		
		if ( !(o instanceof Object) ) {
			return false;
		}
		
		Object other= (Object) o;
		
		return (this.id == other.getId());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 31 * result + (int) (id ^(id >>> 32));
		result = 31 * result + object_name.hashCode();
		
		return result;
	}
}
