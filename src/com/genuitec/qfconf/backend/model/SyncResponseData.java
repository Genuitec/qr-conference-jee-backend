package com.genuitec.qfconf.backend.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SyncResponseData {

	private List<Conference> conferences;
	private List<Attendee> scans;

	@XmlElement(name = "conferences")
	public List<Conference> getConferences() {
		return conferences;
	}

	public void setConferences(List<Conference> conferences) {
		this.conferences = conferences;
	}

	@XmlElement(name = "scans")
	public List<Attendee> getScans() {
		return scans;
	}

	public void setScans(List<Attendee> scans) {
		this.scans = scans;
	}
}
