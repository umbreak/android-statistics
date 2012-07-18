<%@page import="jabx.model.UserTokenTime"%>
<%@page import="utils.AuthManager"%>
<%@page import="utils.HashUtils"%>
<%@page import="jabx.model.UserModel"%>
<%@page import="utils.ServerUtils"%>
<%@page import="hibernate.db.DB_Process"%>
<%@page import="jabx.model.BaseChartModel"%>
<%@page import="jabx.model.ChartModel"%>
<%@page import="java.util.List"%>
<%@page import="jabx.model.BaseChartModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="jabx.model.CategoryModel"%>
<%@page import="hibernate.db.DB_Process"%>
<%@page import="jabx.model.ChartModel"%>
<%@page import="java.util.Random"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.io.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="en-US"
	xml:lang="en">
<head>
<!--
    Created by Artisteer v3.1.0.48375
    Base template (without user's data) checked by http://validator.w3.org : "This page is valid XHTML 1.0 Transitional"
    -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Charts</title>



<link rel="stylesheet" href="style.css" type="text/css" media="screen" />
<!--[if IE 6]><link rel="stylesheet" href="style.ie6.css" type="text/css" media="screen" /><![endif]-->
<!--[if IE 7]><link rel="stylesheet" href="style.ie7.css" type="text/css" media="screen" /><![endif]-->

<script type="text/javascript" src="script.js"></script>
<style type="text/css">
.art-post .layout-item-0 {
	color: #2D3B43;
	border-spacing: 15px 0px;
	border-collapse: separate;
}

.art-post .layout-item-1 {
	border-style: Dashed;
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-top-color: #4F92C4;
	border-right-color: #4F92C4;
	border-bottom-color: #4F92C4;
	border-left-color: #4F92C4;
	color: #1F282E;
	background: repeat #DBE5EB;
	padding-right: 10px;
	padding-left: 10px;
}

.art-post .layout-item-2 {
	color: #2D3B43;
	padding-right: 10px;
	padding-left: 10px;
}

.ie7 .art-post .art-layout-cell {
	border: none !important;
	padding: 0 !important;
}

.ie6 .art-post .art-layout-cell {
	border: none !important;
	padding: 0 !important;
}
</style>

</head>
<body>
	<%
		int logged = 4;
		if (request.getMethod().equalsIgnoreCase("POST")) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			password= HashUtils.i.getHash(password);
			if (!(email.isEmpty() || password.isEmpty())) {
				UserModel user = DB_Process.i.getUser(email);
				if (user == null)
					logged = 1;
				else {
					if (user.getPassword().equals(password)) {
						logged = 0;
						String token= AuthManager.getUniqueDate();
						token=HashUtils.i.getHash(password + token);
						AuthManager.i.getToken_table().put(token, new UserTokenTime(email));
						Cookie cookie = new Cookie ("chemnitz_token",token);

 						response.setHeader("chemnitz_token", token);
 						response.addCookie(cookie);
						response.sendRedirect("index.jsp");
					} else
						logged = 2;
				}
			}else
				logged=3;
		}
	%>

	<div id="art-page-background-glare-wrapper">
		<div id="art-page-background-glare"></div>
	</div>
	<div id="art-main">
		<div class="cleared reset-box"></div>
		<div class="art-header">
			<div class="art-header-position">
				<div class="art-header-wrapper">
					<div class="cleared reset-box"></div>
					<div class="art-header-inner">
						<div class="art-headerobject"></div>
						<div class="art-logo">
							<h1 class="art-logo-name">
								<a href="./index.jsp">Charts Manager</a>
							</h1>
						</div>
					</div>
				</div>
			</div>

		</div>
		<div class="cleared reset-box"></div>
		<div class="art-bar art-nav">
			<div class="art-nav-outer">
				<div class="art-nav-wrapper">
					<div class="art-nav-inner">
						<div class="art-nav-center">
							<ul class="art-hmenu">
								<li><a href="./index.jsp" class="active">Charts</a></li>
								<li><a href="./categories.jsp">Categories</a></li>
								<li><a href="./users.jsp">Users</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="cleared reset-box"></div>
		<div class="art-box art-sheet">
			<div class="art-box-body art-sheet-body">
				<div class="art-layout-wrapper">
					<div class="art-content-layout">
						<div class="art-content-layout-row">
							<div class="art-layout-cell art-content">
								<div class="art-box art-post">

									<div class="art-box-body art-post-body">
										<div class="art-post-inner art-article">
											<div class="art-postcontent">
												<div class="art-content-layout layout-item-0">
													<div class="art-content-layout-row">
														<div class="art-layout-cell layout-item-1"
															style="width: 45%;">
															<h3>Login</h3>

															<p>
																<br />
															</p>

															<form method="post" action="login.jsp">
																<table border="0" cellspacing="0" cellpadding="0"
																	style="width: 100%; margin-top: 2px; margin-bottom: 2px; margin-left: 2px; margin-right: 2px;">
																	<tbody>
																		<tr>
																			<td width="29%">Email:</td>

																			<td width="71%"><input name="email" type="text"
																				class="input" /></td>
																		</tr>
																		<tr>
																			<td width="29%">Password:</td>

																			<td width="71%"><input name="password"
																				type="password" class="input" /></td>
																		</tr>


																		<tr>
																		<tr>
																			<td colspan="2"><br /></td>
																		</tr>
																	</tbody>
																</table>
																<input class="art-button" type="Submit" value="Submit">
																<br /> <br />

															</form>
														</div>
														<div class="art-layout-cell layout-item-2"
															style="width: 55%;">
															Manager for the TU-Charts. It's possible to manage:
															<ul>
																<li>The categories of the charts.</li>
																<li>The charts.</li>
																<li>The users that have access to the charts.</li>
															</ul>
															<% String message=null;
															if (logged == 1) message="User innexistent";
															else if (logged == 2) message="Password incorrect";
															else if (logged == 3) message="User and/or password not filled";
															if (message != null){%>
															<h4><span style="color: red"><%= message%></h4>
															<% }%>
															<div class="cleared"></div>
														</div>

														<div class="cleared"></div>
													</div>
												</div>
												<div class="cleared"></div>
											</div>
										</div>
									</div>
								</div>
								<div class="cleared"></div>
								<div class="art-footer">
									<div class="art-footer-body">
										<div class="art-footer-text">
											<p>
												Copyright © 2012, TU-Charts.<br /> <br />
											</p>
										</div>
										<div class="cleared"></div>
									</div>
								</div>
								<div class="cleared"></div>
							</div>
						</div>
						<div class="cleared"></div>
						<p class="art-page-footer"></p>
						<div class="cleared"></div>
					</div>
</body>
</html>
