<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="hibernate.db.DB_Process"%>
<%@page import="models.CategoryModel"%>
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
<title>Categories</title>



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
<%	String email=(String)session.getAttribute("email");
	if (email == null)
		response.sendRedirect("login.jsp");%>
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
								<li><a href="./categories.jsp" class="active">Categories</a>
								</li>
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

		<%
		if (request.getMethod().equalsIgnoreCase("POST")){
		String name=request.getParameter("name");
		String description=request.getParameter("description");
		if (!name.isEmpty()){
			DB_Process.i.setCategory(new CategoryModel(name,description));
			
		%><h4><span style="color: green">Category <%= name%> added</h4><%
		}
		}else if (request.getParameter("delete") != null){
			int category_id=Integer.parseInt(request.getParameter("delete"));
			DB_Process.i.delCategory(category_id);
			%><h4><span style="color: red">Category Deleted!</h4><%
		}%>
												

												<div class="art-content-layout layout-item-0">
													<div class="art-content-layout-row">
														<div class="art-layout-cell layout-item-2"
															style="width: 45%;">
															<table class="table-3" style="width: 100%;">
																<tbody>
																	<tr>
																		<td>
																			<h3>New Category</h3>

																			<p>
																				<br />
																			</p>

																			<form method="post" action="categories.jsp">
																				<table border="0" cellspacing="0" cellpadding="0"
																					style="width: 100%;">
																					<tbody>
																						<tr>
																							<td>Name:</td>

																							<td><input name="name" type="text"
																								class="input" /></td>
																						</tr>

																						<tr>
																							<td>Description:</td>

																							<td><textarea name="description"
																									class="input">	</textarea></td>
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
															style="width: 55%;">
															<h3>All Categories</h3>
															<%  List<CategoryModel> categories= DB_Process.i.getCategories();%>
															<br />

															<table id="rounded-corner"
																summary="2007 Major IT Companies' Profit" border="0"
																cellspacing="0" cellpadding="0" style="width: 100%;">

																<thead>

																	<tr>

																		<th scope="col" class="rounded-company">Name</th>

																		<th scope="col" class="rounded-q2">Description</th>

																		<th scope="col" class="rounded-q2">Num. Charts</th>
																		<th scope="col" class="rounded-q4">Manager</th>


																	</tr>

																</thead>

																<tfoot>

																	<tr>

																		<td colspan="3" class="rounded-foot-left"><em>Categories data obtained from the Database. The manager is able to Modify or Delete categories. More info about the database structure <a href="out/">HERE</a></em></td>

																		<td class="rounded-foot-right">&nbsp;</td>

																	</tr>

																</tfoot>

																<tbody>
																	<%for (CategoryModel category: categories) {
	%>
																	<tr>
																		<td><%=category.getName()%></td>
																		<td><%= category.getDescription()%></td>
																		<td><%= category.getCharts().size()%></td>
																		<td><a href="<%=request.getServletPath().replace("/", "") + "?edit="+category.getId() %>" /><img src="images/edit.png" alt="Edit Chart"/></a> 
																		<a href="<%=request.getServletPath().replace("/", "") + "?delete="+category.getId() %>" /><img src="images/delete.png" alt="Delete Chart"/></a></td>
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
