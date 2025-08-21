package com.serhat.security;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletResponse httpResp = (HttpServletResponse) response;

		httpResp.setHeader("Cache-control", "no-cache, no-store, must-revalidate");
		httpResp.setHeader("Pragma", "no-cache");
		httpResp.setHeader("Expires", "0");

		chain.doFilter(request, response);

	}
}
