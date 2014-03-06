package com.genuitec.qfconf.backend.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SyncLists<E> {

	private List<E> update;
	private List<E> create;
	private long lastSyncTime;

	@XmlElement(name = "update")
	public List<E> getUpdate() {
		return update;
	}

	public void setUpdate(List<E> update) {
		this.update = update;
	}

	@XmlElement(name = "create")
	public List<E> getCreate() {
		return create;
	}

	public void setCreate(List<E> create) {
		this.create = create;
	}

	@XmlElement(name = "lastSyncTime")
	public long getLastSyncTime() {
		return lastSyncTime;
	}

	public void setLastSyncTime(long lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
}