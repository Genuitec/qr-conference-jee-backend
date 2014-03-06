package com.genuitec.qfconf.backend.model;

import javax.xml.bind.annotation.XmlElement;

public class SyncResponse {

	private SyncResponseInfo info;
	private SyncResponseData data;

	@XmlElement(name = "info")
	public SyncResponseInfo getInfo() {
		return info;
	}

	public void setInfo(SyncResponseInfo info) {
		this.info = info;
	}

	@XmlElement(name = "data")
	public SyncResponseData getData() {
		return data;
	}

	public void setData(SyncResponseData data) {
		this.data = data;
	}
}
