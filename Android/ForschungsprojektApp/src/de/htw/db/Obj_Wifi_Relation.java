package de.htw.db;

public class Obj_Wifi_Relation {

	private long id;
	private long obj_fk;
	private long wifi_fk;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getObj_fk() {
		return obj_fk;
	}
	public void setObj_fk(long obj_fk) {
		this.obj_fk = obj_fk;
	}
	public long getWifi_fk() {
		return wifi_fk;
	}
	public void setWifi_fk(long wifi_fk) {
		this.wifi_fk = wifi_fk;
	}
	
	
}
