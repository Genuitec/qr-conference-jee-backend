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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.genuitec.qfconf.backend.model.Attendee;
import com.genuitec.qfconf.backend.model.Conference;
import com.genuitec.qfconf.backend.model.ConferenceModel;
import com.genuitec.qfconf.backend.model.SyncRequest;
import com.genuitec.qfconf.backend.model.SyncResponse;
import com.genuitec.qfconf.backend.model.SyncResponseData;
import com.genuitec.qfconf.backend.model.SyncResponseInfo;

@Consumes("application/json")
@Produces("application/json")
@Path("mobile")
public class MobileService {

	private Logger log = Logger.getLogger(MobileService.class.getName());

	@GET
	@Path("login")
	public String login() {
		return "already-logged-in";
	}

	@POST
	@Path("sync")
	public SyncResponse sync(SyncRequest request) {
		EntityManager em = ConferenceModel.newEntityManager();
		try {
			List<Conference> confs = em.createQuery(
					"SELECT c FROM Conference c WHERE c.syncTime > "
							+ request.getInfo().getTime()
							+ " ORDER BY c.startsOn DESC", Conference.class)
					.getResultList();

			List<Attendee> scans = em.createQuery(
					"SELECT a FROM Attendee a WHERE a.syncTime > "
							+ request.getInfo().getTime()
							+ " ORDER BY a.lastName, a.firstName",
					Attendee.class).getResultList();

			SyncResponse response = new SyncResponse();
			response.setInfo(new SyncResponseInfo());
			response.getInfo().setTime(System.currentTimeMillis());
			response.setData(new SyncResponseData());

			if (!confs.isEmpty())
				response.getData().setConferences(confs);
			if (!scans.isEmpty())
				response.getData().setScans(scans);

			return response;
		} finally {
			em.close();
		}
	}

	@GET
	@Path("{conference}/{attendee}")
	public Attendee getAttendee(@PathParam("conference") int conferenceID,
			@PathParam("attendee") int attendeeID) {
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
	@Consumes("application/xml")
	@Produces("text/html")
	public String addAttendee(Attendee attendee) {
		EntityManager em = ConferenceModel.newEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(attendee);
			em.getTransaction().commit();
			log.log(Level.INFO, "Added attendee with ID {0}: {1} {2}",
					new Object[] { attendee.getId(), attendee.getFirstName(),
							attendee.getLastName() });
			return "added-attendee: " + attendee.getId();
		} finally {
			em.close();
		}
	}
}