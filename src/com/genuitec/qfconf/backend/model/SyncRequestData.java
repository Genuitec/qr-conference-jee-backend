package com.genuitec.qfconf.backend.model;

import javax.xml.bind.annotation.XmlElement;

public class SyncRequestData {

	private SyncLists<Conference> conferences;
	private SyncLists<Attendee> scans;

	@XmlElement(name = "conferences")
	public SyncLists<Conference> getConferences() {
		return conferences;
	}

	public void setConferences(SyncLists<Conference> conferences) {
		this.conferences = conferences;
	}

	@XmlElement(name = "scans")
	public SyncLists<Attendee> getScans() {
		return scans;
	}

	public void setScans(SyncLists<Attendee> scans) {
		this.scans = scans;
	}
}
