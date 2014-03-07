package com.genuitec.qfconf.backend.ws;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCrossDomainResponseHeader implements Filter {

	@Override
	public void destroy() {
		// no config
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse sr = (HttpServletResponse) response;
		sr.setHeader("Access-Control-Allow-Origin", "*");
		sr.setHeader("Access-Control-Allow-Methods", "GET, POST");
		sr.setHeader("Access-Control-Allow-Headers", "accept, content-type");
		if (!"OPTIONS".equals(((HttpServletRequest) request).getMethod()))
			chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// no config
	}
}
