package de.htw.db;

public class Obj_Bt_Relation {

	private long id;
	private long obj_fk;
	private long bt_fk;
	
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
	public long getBt_fk() {
		return bt_fk;
	}
	public void setBt_fk(long bt_fk) {
		this.bt_fk = bt_fk;
	}
	
	
}
