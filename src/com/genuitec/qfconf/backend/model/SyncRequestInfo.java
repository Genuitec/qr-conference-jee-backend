package com.genuitec.qfconf.backend.model;

import javax.xml.bind.annotation.XmlElement;

public class SyncRequestInfo {

	private long time;

	@XmlElement(name = "time")
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
