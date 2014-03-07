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
			request.getSession(true); // force session to exist
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

		EntityManager em = ConferenceModel.newEntityManager();
		try {
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

	private void addCrossDomainHeaders() {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST");
		response.setHeader("Access-Control-Allow-Headers",
				"accept, content-type");
	}
}