package br.com.htecon.persistent;

import java.io.Serializable;

public class ExEntity implements Serializable {
	
	private static final long serialVersionUID = 1404761991632340834L;

	public final static String EX_STATUS_INSERTED = "I";
	public final static String EX_STATUS_UPDATED = "U";
	public final static String EX_STATUS_DELETED = "R";

	private String status;
	

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public void setStatusInserted() {
		setStatus(EX_STATUS_INSERTED);
	}
	
	public void setStatusUpdated() {
		setStatus(EX_STATUS_UPDATED);
	}
	
	public void setStatusDeleted() {
		setStatus(EX_STATUS_DELETED);
	}
	
	public boolean isStatusInserted() {
		return EX_STATUS_INSERTED.equals(getStatus());
	}
	
	public boolean isStatusUpdated() {
		return EX_STATUS_UPDATED.equals(getStatus());
	}	
	
	public boolean isStatusDeleted() {
		return EX_STATUS_DELETED.equals(getStatus());
	}	
	

}
