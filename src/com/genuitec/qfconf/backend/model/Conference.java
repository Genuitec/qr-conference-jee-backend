/*******************************************************************************
 *  Copyright (c) 2014 Genuitec, LLC.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *     Genuitec, LLC - initial API and implementation using MyEclipse
 *******************************************************************************/
package com.genuitec.qfconf.backend.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.genuitec.qfconf.backend.serialize.YYYYMMDDDateDeserializer;
import com.genuitec.qfconf.backend.serialize.YYYYMMDDDateSerializer;

@Entity
@XmlRootElement
@JsonInclude(Include.ALWAYS)
public class Conference implements Comparable<Conference> {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	@Temporal(TemporalType.DATE)
	private Date startsOn;
	@Temporal(TemporalType.DATE)
	private Date endsOn;
	private long syncTime;
	private Set<String> tags;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartsOn() {
		return startsOn;
	}

	@XmlElement(name = "time")
	@JsonDeserialize(using = YYYYMMDDDateDeserializer.class)
	@JsonSerialize(using = YYYYMMDDDateSerializer.class)
	public void setStartsOn(Date startsOn) {
		this.startsOn = startsOn;
	}

	public Date getEndsOn() {
		return endsOn;
	}

	@XmlElement(name = "endtime")
	@JsonDeserialize(using = YYYYMMDDDateDeserializer.class)
	@JsonSerialize(using = YYYYMMDDDateSerializer.class)
	public void setEndsOn(Date endsOn) {
		this.endsOn = endsOn;
	}

	@Override
	public int compareTo(Conference arg0) {
		return arg0.startsOn.compareTo(this.startsOn);
	}

	@XmlElement(name = "synctime")
	public long getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}

	@XmlJavaTypeAdapter(TagsAdapter.class)
	@XmlElement(name = "tags")
	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
}