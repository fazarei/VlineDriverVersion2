<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	final String portalBaseUrl = "http://localhost:8080/ekp/specsavers";
	final String ekpBaseUrl = "http://localhost:8080/ekp/servlet/ekp";

	String nonce = Long.toString(SecureRandom.getInstance("SHA1PRNG").nextLong());
	String callback = portalBaseUrl + "/authcallback-general.jsp";
	session.setAttribute("nonce", nonce);
	response
			.sendRedirect(ekpBaseUrl + "/authenticate?nonce="
					+ URLEncoder.encode(nonce, "UTF-8")
					+ "&callback="
					+ URLEncoder.encode(callback, "UTF-8"));
%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.security.SecureRandom"%>