package com.genuitec.qfconf.backend.model;

import javax.xml.bind.annotation.XmlElement;

/*
 * {
 *  info:{},
 *  data:{
 *      'tableName' : {
 *          update: [],
 *          create: [],
 *          lastSyncTine: 12321321321
 *      },
 *      'tableName' : {
 *          update: [],
 *          create: [],
 *          lastSyncTine: 12321321321
 *      },
 *  }
 * }
 */
public class SyncRequest {

	private SyncRequestInfo info;
	private SyncRequestData data;

	@XmlElement(name = "info")
	public SyncRequestInfo getInfo() {
		return info;
	}

	public void setInfo(SyncRequestInfo info) {
		this.info = info;
	}

	@XmlElement(name = "data")
	public SyncRequestData getData() {
		return data;
	}

	public void setData(SyncRequestData data) {
		this.data = data;
	}

}
