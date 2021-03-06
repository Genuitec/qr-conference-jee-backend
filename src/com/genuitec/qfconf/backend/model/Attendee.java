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
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.annotations.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.genuitec.qfconf.backend.serialize.BooleanDeserializer;
import com.genuitec.qfconf.backend.serialize.BooleanSerializer;
import com.genuitec.qfconf.backend.serialize.RatingsDeserializer;
import com.genuitec.qfconf.backend.serialize.RatingsSerializer;
import com.genuitec.qfconf.backend.serialize.YYYYMMDDHHMMSSDateDeserializer;
import com.genuitec.qfconf.backend.serialize.YYYYMMDDHHMMSSDateSerializer;

@Entity
@XmlRootElement
@JsonIgnoreProperties({ "sid", "table_name", "time", "rowcreated", "row_id",
		"syncupdatetime", "scannedby_name" })
@JsonInclude(Include.ALWAYS)
public class Attendee {

	// identifying data
	@Id
	private String id;
	@Index
	private long syncTime;
	private int conferenceID;

	// information captured from QR code
	private String fn; // TODO remove when mobile app updated
	private String firstName;
	private String lastName;
	private String organization;
	private String title;
	private String telephone;
	private String cell;
	private String email;
	private String website;
	private String adr; // TODO remove when mobile app updated
	private String street;
	private String city;
	private String state;
	private String postcode;
	private String country;
	private String type;
	private String version;
	private String qrcodetext;

	// data specified by employee at conference
	private String employee;
	@Temporal(TemporalType.DATE)
	private Date scannedAt;
	@Temporal(TemporalType.DATE)
	private Date modifiedAt;
	private Rating rating = Rating.cold;
	private SortedSet<String> tags;
	private String notes;
	private boolean followup;
	private String lastChangedBy;

	@JsonInclude(Include.ALWAYS)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name = "conference_id")
	@JsonInclude(Include.ALWAYS)
	public int getConferenceID() {
		return conferenceID;
	}

	public void setConferenceID(int conferenceID) {
		this.conferenceID = conferenceID;
	}

	@XmlElement(name = "firstname")
	@JsonInclude(Include.ALWAYS)
	public String getFirstName() {
		if (lastName == null && firstName == null && fn != null) {
			if (fn.indexOf(' ') > 0)
				return fn.substring(0, fn.indexOf(' ')).trim();
			if (fn.indexOf(',') > 0)
				return fn.substring(fn.indexOf(',') + 1).trim();
		}
		return firstName;
	}

	public void setFirstName(String first) {
		this.firstName = first;
	}

	@XmlElement(name = "lastname")
	@JsonInclude(Include.ALWAYS)
	public String getLastName() {
		if (lastName == null && firstName == null && fn != null) {
			if (fn.indexOf(' ') > 0)
				return fn.substring(fn.indexOf(' ') + 1);
			if (fn.indexOf(',') > 0)
				return fn.substring(0, fn.indexOf(',')).trim();
		}
		return lastName;
	}

	public void setLastName(String last) {
		this.lastName = last;
	}

	@XmlElement(name = "org")
	@JsonInclude(Include.ALWAYS)
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@XmlElement(name = "title")
	@JsonInclude(Include.ALWAYS)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "tel")
	@JsonInclude(Include.ALWAYS)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@XmlElement(name = "cel")
	@JsonInclude(Include.ALWAYS)
	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	@JsonInclude(Include.ALWAYS)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonInclude(Include.ALWAYS)
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@JsonInclude(Include.ALWAYS)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@JsonInclude(Include.ALWAYS)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@JsonInclude(Include.ALWAYS)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@JsonInclude(Include.ALWAYS)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@JsonInclude(Include.ALWAYS)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@XmlElement(name = "scannedby_id")
	@JsonInclude(Include.ALWAYS)
	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	@JsonDeserialize(using = YYYYMMDDHHMMSSDateDeserializer.class)
	@JsonSerialize(using = YYYYMMDDHHMMSSDateSerializer.class)
	@XmlElement(name = "scantime")
	@JsonInclude(Include.ALWAYS)
	public Date getScannedAt() {
		return scannedAt;
	}

	public void setScannedAt(Date scannedAt) {
		this.scannedAt = scannedAt;
	}

	@JsonDeserialize(using = YYYYMMDDHHMMSSDateDeserializer.class)
	@JsonSerialize(using = YYYYMMDDHHMMSSDateSerializer.class)
	@XmlElement(name = "updatetime")
	@JsonInclude(Include.ALWAYS)
	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@JsonSerialize(using = RatingsSerializer.class)
	@JsonDeserialize(using = RatingsDeserializer.class)
	@JsonInclude(Include.ALWAYS)
	public Rating getRating() {
		if (rating == null)
			return Rating.cold;
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	@XmlJavaTypeAdapter(TagsAdapter.class)
	@XmlElement(name = "tags")
	@JsonInclude(Include.ALWAYS)
	public SortedSet<String> getTags() {
		return tags;
	}

	public void setTags(SortedSet<String> tags) {
		this.tags = tags;
	}

	@JsonInclude(Include.ALWAYS)
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@XmlElement(name = "synctime")
	@JsonInclude(Include.ALWAYS)
	public long getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(long syncID) {
		this.syncTime = syncID;
	}

	@JsonSerialize(using = BooleanSerializer.class)
	@JsonDeserialize(using = BooleanDeserializer.class)
	@JsonInclude(Include.ALWAYS)
	public boolean isFollowup() {
		return followup;
	}

	public void setFollowup(boolean followup) {
		this.followup = followup;
	}

	@XmlElement(name = "fn")
	@JsonInclude(Include.ALWAYS)
	public String getFullname() {
		return fn;
	}

	public void setFullname(String fn) {
		this.fn = fn;
	}

	@XmlElement(name = "adr")
	@JsonInclude(Include.ALWAYS)
	public String getAddress() {
		return adr;
	}

	public void setAddress(String adr) {
		this.adr = adr;
	}

	@JsonInclude(Include.ALWAYS)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@JsonInclude(Include.ALWAYS)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void updateTo(Attendee latest) {
		this.modifiedAt = latest.modifiedAt;
		this.rating = latest.rating;
		this.tags = latest.tags;
		this.notes = latest.notes;
		this.followup = latest.followup;
	}

	public void mergeWith(Attendee latest) {
		if (this.scannedAt.compareTo(latest.scannedAt) > 0)
			this.scannedAt = latest.scannedAt;
		if (this.modifiedAt.compareTo(latest.modifiedAt) < 0)
			this.modifiedAt = latest.modifiedAt;
		if (this.getRating().compareTo(latest.getRating()) < 0)
			this.rating = latest.getRating();
		if (this.tags != null && latest.tags != null)
			this.tags.addAll(latest.tags);
		else if (latest.tags != null)
			this.tags = latest.tags;
		if (this.notes != null && this.notes.equals(latest.notes)) {
			// nothing to merge
		} else if (this.notes != null && this.notes.length() > 0
				&& latest.notes != null) {
			this.notes += "\n\n" + latest.notes;
		}
		this.followup = latest.followup || this.followup;
	}

	@XmlElement(name = "qrcodetext")
	@JsonInclude(Include.ALWAYS)
	@Column(name = "qrraw", length = 2048)
	public String getQRCodeText() {
		return qrcodetext;
	}

	public void setQRCodeText(String qrcodetext) {
		this.qrcodetext = qrcodetext;
	}

	@JsonIgnore
	public void setLastChangedBy(String lastChangedBy) {
		this.lastChangedBy = lastChangedBy;
	}

	public String getLastChangedBy() {
		return lastChangedBy;
	}
}
