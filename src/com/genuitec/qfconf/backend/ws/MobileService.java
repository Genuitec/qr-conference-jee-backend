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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import com.genuitec.qfconf.backend.model.Attendee;
import com.genuitec.qfconf.backend.model.Conference;
import com.genuitec.qfconf.backend.model.ConferenceModel;
import com.genuitec.qfconf.backend.model.LoginResult;
import com.genuitec.qfconf.backend.model.SyncLists;
import com.genuitec.qfconf.backend.model.SyncRequest;
import com.genuitec.qfconf.backend.model.SyncResponse;
import com.genuitec.qfconf.backend.model.SyncResponseData;
import com.genuitec.qfconf.backend.model.SyncResponseInfo;

@Consumes("application/json")
@Produces("application/json")
@Path("mobile")
public class MobileService {

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("login")
	public LoginResult login(@FormParam("j_username") String user,
			@FormParam("j_password") String password) {
		addCrossDomainHeaders();

		LoginResult result = new LoginResult();
		try {
			if (request.getUserPrincipal() != null)
				request.logout();

			request.getSession(true); // force session to exist
			request.getSession().setMaxInactiveInterval(-1);
			request.getSession().setAttribute("logged-on-at",
					System.currentTimeMillis());
			request.login(user, password);
			result.setSession(request.getSession().getId());
			result.setLoggedIn(true);
		} catch (Exception e) {
			result.setLoggedIn(false);
			result.setReason("Incorrect username or password.");
		}
		return result;
	}

	@GET
	@Path("logout")
	public LoginResult logout() {
		addCrossDomainHeaders();

		LoginResult result = new LoginResult();
		try {
			request.logout();
		} catch (Exception e) {
		}
		return result;
	}

	@POST
	@Path("sync")
	public SyncResponse sync(SyncRequest sync) {
		addCrossDomainHeaders();

		if (request.getUserPrincipal() == null)
			throw new WebApplicationException(403);

		request.getSession().setAttribute("last-access-at",
				System.currentTimeMillis());

		EntityManager em = ConferenceModel.newEntityManager();
		try {

			updateAttendees(em, sync);

			List<Conference> confs = em.createQuery(
					"SELECT c FROM Conference c WHERE c.syncTime > "
							+ sync.getInfo().getTime()
							+ " ORDER BY c.startsOn DESC", Conference.class)
					.getResultList();

			List<Attendee> scans = em.createQuery(
					"SELECT a FROM Attendee a WHERE a.syncTime > "
							+ sync.getInfo().getTime()
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

	private void updateAttendees(EntityManager em, SyncRequest sync) {
		long synctime = System.currentTimeMillis();

		SyncLists<Attendee> scansData = sync.getData().getScans();
		if (scansData != null) {
			Map<String, Attendee> updatedAttendees = new HashMap<String, Attendee>();
			mergeIn(updatedAttendees, scansData.getCreate());
			mergeIn(updatedAttendees, scansData.getUpdate());
			if (!updatedAttendees.isEmpty()) {
				em.getTransaction().begin();
				for (Attendee next : updatedAttendees.values())
					updateOrCreate(em, next, synctime);
				em.getTransaction().commit();
			}
		}
	}

	private void updateOrCreate(EntityManager em, Attendee latest, long synctime) {
		Attendee existing = em.find(Attendee.class, latest.getId());
		if (existing == null) {
			latest.setSyncTime(synctime);
			em.persist(latest);
		} else if (existing.getSyncTime() != latest.getSyncTime()) {
			existing.mergeWith(latest);
			existing.setSyncTime(synctime);
			em.persist(existing);
		} else {
			existing.updateTo(latest);
			existing.setSyncTime(synctime);
			em.persist(existing);
		}
	}

	private void mergeIn(Map<String, Attendee> updatedAttendees,
			List<Attendee> attendees) {
		for (Attendee next : attendees) {
			Attendee current = updatedAttendees.get(next.getId());
			if (current == null
					|| current.getModifiedAt().compareTo(next.getModifiedAt()) < 0) {
				updatedAttendees.put(next.getId(), next);
			}
		}
	}

	private void addCrossDomainHeaders() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST");
		response.setHeader("Access-Control-Allow-Headers",
				"accept, content-type");
	}
}