<%@page import="com.google.common.primitives.Ints"%>
<%@page import="jabx.model.UserModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="hibernate.db.DB_Process"%>
<%@page import="java.util.List"%>
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
<title>Users</title>



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

.table-3 {
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
							<h1 class="art-logo-name"><a href="./index.html">Charts Manager</a></h1>
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
								<li><a href="./index.jsp">Charts</a></li>
								<li><a href="./categories.jsp" >Categories</a>
								</li>
								<li><a href="./users.jsp" class="active">Users</a></li>
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

		<%
		if (request.getMethod().equalsIgnoreCase("POST")){
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		String email=request.getParameter("email");
		if (!email.isEmpty()){
			DB_Process.i.setUser(new UserModel(username,password,email));
			
		%><h4><span style="color: green">User <%= username%> added</h4><%
		}
		}else if (request.getParameter("delete") != null){
			String email=request.getParameter("delete");
			DB_Process.i.delUser(email);
			%><h4><span style="color: red">User with email <%= email%> Deleted!</h4><%
		}%>
												

												<div class="art-content-layout layout-item-0">
													<div class="art-content-layout-row">
														<div class="art-layout-cell layout-item-2"
															style="width: 40%;">
															<table class="table-3" style="width: 100%;">
																<tbody>
																	<tr>
																		<td>
																			<h3>New User</h3>

																			<p>
																				<br />
																			</p>

																			<form method="post" action="users.jsp">
																				<table border="0" cellspacing="0" cellpadding="0"
																					style="width: 100%;">
																					<tbody>
																						<tr>
																							<td>Username:</td>

																							<td><input name="username" type="text"
																								class="input" /></td>
																						</tr>
																						<tr>
																							<td>Password:</td>

																							<td><input name="password" type="password"
																								class="input" /></td>
																						</tr>
																						<tr>
																							<td>Email:</td>

																							<td><input name="email" type="text"
																								class="input" /></td>
																						</tr>																																												
																						<tr>
																							<td colspan="2"><br /></td>
																						</tr>
																					</tbody>
																				</table>
																				<input class="art-button" type="Submit"
																					value="Submit"> <br /> <br />
																			</form>
																		</td>
																	</tr>
																</tbody>
															</table>
														</div>
														<div class="art-layout-cell layout-item-2"
															style="width: 60%;">
															<h3>All Users</h3>
															<%  List<UserModel> users= DB_Process.i.getUsers();%>
															<br />

															<table id="rounded-corner"
																summary="2007 Major IT Companies' Profit" border="0"
																cellspacing="0" cellpadding="0" style="width: 100%;">
																<thead>
																	<tr>
																		<th scope="col" class="rounded-company">Username</th>
																		<th scope="col" class="rounded-q2">Email</th>
																		<th scope="col" class="rounded-q2">Cat. Denied</th>
																		<th scope="col" class="rounded-q2">Charts Denied</th>	
																		<th scope="col" class="rounded-q4">Manager</th>
																	</tr>
																</thead>
																<tfoot>
																	<tr>
																		<td colspan="4" class="rounded-foot-left"><em>Users data obtained from the Database. The manager is able to Modify or Delete users. More info about the database structure <a href="out/">HERE</a></em></td>
																		<td class="rounded-foot-right">&nbsp;</td>

																	</tr>

																</tfoot>

																<tbody>
																	<%for (UserModel user: users) {
	%>
																	<tr>
																		<td><%=user.getUsername()%></td>
																		<td><%= user.getEmail()%></td>
																		<%String charts_denied="";
																		try{
																			charts_denied=Ints.join(",", user.getCharts_denied());
																			}catch (NullPointerException e){}
																		String categories_denied="";
																		try{
																			categories_denied=Ints.join(",", user.getCategories_denied());
																			}catch (NullPointerException e){}
																		%>
																		
																		<td><%=categories_denied %></td>
																		<td><%= charts_denied%></td>
																		<td><a href="<%=request.getServletPath().replace("/", "") + "?edit="+user.getEmail() %>" /><img src="images/edit.png" alt="Edit Chart"/></a> 
																		<a href="<%=request.getServletPath().replace("/", "") + "?delete="+user.getEmail() %>" /><img src="images/delete.png" alt="Delete Chart"/></a></td>
																	</tr>

																	<%}%>

																</tbody>

															</table>

														</div>
													</div>
												</div>

											</div>
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
								<br />
							</p>
						</div>
						<div class="cleared"></div>
					</div>
				</div>
				<div class="cleared"></div>
			</div>
		</div>
		<div class="cleared"></div>
		<p class="art-page-footer">

		</p>
		<div class="cleared"></div>
	</div>

</body>
</html>
