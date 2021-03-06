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
package com.genuitec.qfconf.backend.ws;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import com.genuitec.qfconf.backend.model.AddResult;
import com.genuitec.qfconf.backend.model.Attendee;
import com.genuitec.qfconf.backend.model.ConferenceModel;
import com.genuitec.qfconf.backend.model.DataTableResult;

@Produces({ "application/xml", "application/json" })
@Path("attendees")
public class AttendeesResource {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	private Logger log = Logger.getLogger(AttendeesResource.class.getName());

	@GET
	@Path("{conference}")
	public List<Attendee> getAttendees(@PathParam("conference") int conferenceID) {
		EntityManager em = ConferenceModel.newEntityManager();
		try {
			List<Attendee> confs = em
					.createQuery(
							"SELECT a FROM Attendee a WHERE a.conferenceID="
									+ conferenceID
									+ " ORDER BY a.organization, a.lastName, a.firstName",
							Attendee.class).getResultList();
			log.log(Level.INFO,
					"Responding with {0} attendees for conference ID {1}",
					new Object[] { confs.size(), conferenceID });
			return confs;
		} finally {
			em.close();
		}
	}

	@GET
	@Path("{conference}/datatable")
	@Produces("application/json")
	public DataTableResult getAttendeesDatatable(
			@PathParam("conference") int conferenceID) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		DataTableResult result = new DataTableResult();
		for (Attendee attendee : getAttendees(conferenceID)) {
			List<String> rowData = new ArrayList<String>();
			rowData.add(String.valueOf(attendee.getId()));
			rowData.add(attendee.getOrganization());
			rowData.add(attendee.getFirstName());
			rowData.add(attendee.getLastName());
			rowData.add(attendee.getTitle());
			rowData.add(describeFollowup(attendee));
			rowData.add(describeRating(attendee));
			rowData.add(describeTags(attendee));
			rowData.add(dateFormat.format(attendee.getScannedAt()));
			rowData.add(attendee.getEmployee());
			rowData.add(describeNotes(attendee));
			result.addRowData(rowData);
		}
		return result;
	}

	@GET
	@Path("{conference}/attendees.tsv")
	@Produces("text/tab-separated-values")
	public String getAttendeesTSV(@PathParam("conference") int conferenceID) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		response.setHeader(
				"Content-Disposition",
				"attachment;filename=attendees-"
						+ dateFormat.format(new Date()) + ".tsv");
		dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		out.println("first\tlast\tphone\temail\tscannedby\tscannedon\tfollowup\trating\ttags\tnotes");
		for (Attendee attendee : getAttendees(conferenceID)) {
			out.print(attendee.getFirstName());
			out.print('\t');
			out.print(attendee.getLastName());
			out.print('\t');
			out.print(notnull(attendee.getTelephone()));
			out.print('\t');
			out.print(notnull(attendee.getEmail()));
			out.print('\t');
			out.print(attendee.getEmployee());
			out.print('\t');
			out.print(dateFormat.format(attendee.getScannedAt()));
			out.print('\t');
			out.print(describeFollowup(attendee));
			out.print('\t');
			out.print(describeRating(attendee));
			out.print('\t');
			out.print(describeTags(attendee));
			out.print('\t');
			if (attendee.getNotes() != null)
				out.print(attendee.getNotes().replace('\n', ' ')
						.replace('\r', ' '));
			out.println();
		}
		return sw.toString();
	}

	private String notnull(String value) {
		if (value == null)
			return "";
		return value;
	}

	@GET
	@Path("{conference}/{attendee}")
	public Attendee getAttendee(@PathParam("conference") int conferenceID,
			@PathParam("attendee") String attendeeID) {
		EntityManager em = ConferenceModel.newEntityManager();
		try {
			Attendee attendee = em.find(Attendee.class, attendeeID);
			if (attendee == null)
				log.log(Level.INFO, "Unable to find attendee with ID {0}",
						new Object[] { attendeeID });
			else if (attendee.getConferenceID() != conferenceID)
				log.log(Level.INFO,
						"Mismatch for conference ID for attendee with ID {0}",
						new Object[] { attendeeID });
			else
				log.log(Level.INFO, "Found attendee with ID {0}: {1} {2}",
						new Object[] { attendeeID, attendee.getFirstName(),
								attendee.getLastName() });
			return attendee;
		} finally {
			em.close();
		}
	}

	@POST
	@Path("add")
	@Consumes({ "application/xml", "application/json" })
	@Produces({ "application/xml", "application/json" })
	public AddResult addAttendee(Attendee attendee) {
		EntityManager em = ConferenceModel.newEntityManager();
		try {
			if (attendee.getId() == null || attendee.getId().length() == 0) {
				attendee.setId("man"
						+ (attendee.getFirstName() + "_" + attendee
								.getLastName()).hashCode());
			}
			if (attendee.getFullname() == null)
				attendee.setFullname(attendee.getFirstName() + " "
						+ attendee.getLastName());
			attendee.setSyncTime(System.currentTimeMillis());
			if (request != null) {
				attendee.setLastChangedBy(request.getUserPrincipal().getName());
				attendee.setEmployee(request.getUserPrincipal().getName());
			}
			em.getTransaction().begin();
			em.persist(attendee);
			em.getTransaction().commit();
			log.log(Level.INFO, "Added attendee with ID {0}: {1} {2}",
					new Object[] { attendee.getId(), attendee.getFirstName(),
							attendee.getLastName() });
			return new AddResult(true, -1);
		} finally {
			em.close();
		}
	}

	public long getAttendeeCount(int conferenceID) {
		EntityManager em = ConferenceModel.newEntityManager();
		try {
			Object result = em.createQuery(
					"SELECT count(a) FROM Attendee a WHERE a.conferenceID="
							+ conferenceID).getSingleResult();
			return (Long) result;
		} finally {
			em.close();
		}
	}

	private String describeNotes(Attendee attendee) {
		String notes = attendee.getNotes();
		notes = notes != null && notes.length() > 0 ? "Has Notes" : "";
		return notes;
	}

	private String describeTags(Attendee attendee) {
		String tags = "";
		if (attendee.getTags() != null) {
			for (String next : attendee.getTags()) {
				if (tags.length() > 0)
					tags += ", ";
				tags += next;
			}
		}
		return tags;
	}

	private String describeRating(Attendee attendee) {
		if (attendee.getRating() == null)
			return "Cold";
		String rating = attendee.getRating().toString();
		if (rating == null)
			rating = "cold";
		rating = Character.toUpperCase(rating.charAt(0)) + rating.substring(1);
		return rating;
	}

	private String describeFollowup(Attendee attendee) {
		return attendee.isFollowup() ? "Follow-up" : "";
	}
}