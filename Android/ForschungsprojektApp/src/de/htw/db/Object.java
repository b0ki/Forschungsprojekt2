package de.htw.db;

import de.htw.bluetooth.RemoteBluetoothDevice;

/**
 * This class is our model and contains the data we will save in the database.
 * @author christian
 *
 */
public class Object {

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
		if (this == o) {
			return true;
		}
		
		if ( !(o instanceof Object) ) {
			return false;
		}
		
		Object other= (Object) o;
		
		return (this.id == other.getId());
	}
}
