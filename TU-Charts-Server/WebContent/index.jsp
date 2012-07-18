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
															style="width: 65%;">
															<h3>New Chart</h3>

															<p>
																<br />
															</p>

															<form enctype="multipart/form-data" method="post"
																action="index.jsp">
																<table border="0" cellspacing="0" cellpadding="0"
																	style="width: 100%; margin-top: 2px; margin-bottom: 2px; margin-left: 2px; margin-right: 2px;">
																	<tbody>
																		<tr>
																			<td width="29%">Name:</td>

																			<td width="71%"><input name="name" type="text"
																				class="input" /></td>
																		</tr>

																		<tr>
																			<td>Description:</td>

																			<td><textarea name="description" class="input"></textarea></td>
																		</tr>

																		<tr>
																			<td>Category:</td>

																			<td><select name="category">
																					<option>Internet</option>
																					<option selected="selected">Electronics</option>
																					<option>Physics</option>
																					<option>Nanotechnology</option>
																					<option>Signal Processing</option>
																			</select></td>
																		</tr>
																		<tr>
																			<td>Type:</td>

																			<td><select name="type">
																					<option>Column</option>
																					<option selected="selected">Line</option>
																					<option>Pie</option>
																					<option>Bar</option>
																					<option>Area</option>
																					<option>Scatter</option>
																			</select></td>
																		</tr>
																		<tr>
																			<td>Values:</td>

																			<td><input type="file" name="data"></td>
																		</tr>
																		<tr>
																			<td colspan="2"><br /></td>
																		</tr>
																	</tbody>
																</table>
																<input class="art-button" type="Submit" value="Submit">
																<br />
																<br />

															</form>
														</div>
														<div class="art-layout-cell layout-item-2"
															style="width: 35%;">
															<%
																//to get the content type information from JSP Request Header
																													String contentType = request.getContentType();
																													Map<String, String> map = new HashMap<String, String>();
																													//here we are checking the content type is not equal to Null and as well 
																													// as the passed data from mulitpart/form-data is greater than or equal to 0
																													if ((contentType != null)
																															&& (contentType.indexOf("multipart/form-data") >= 0)) {
																														Random rand = new Random();
																														DiskFileItemFactory factory = new DiskFileItemFactory();
/* 																														factory.setRepository(repository);
 */																														ServletFileUpload upload = new ServletFileUpload(factory);

																														// req es la HttpServletRequest que recibimos del formulario.
																														// Los items obtenidos serán cada uno de los campos del formulario,
																														// tanto campos normales como ficheros subidos.

																														List<FileItem> items = upload.parseRequest(request);
																														File fichero = null;
																														// Se recorren todos los items, que son de tipo FileItem
																														for (Object item : items) {
																															FileItem uploaded = (FileItem) item;

																															// Hay que comprobar si es un campo de formulario. Si no lo es, se guarda el fichero
																															// subido donde nos interese
																															if (!uploaded.isFormField()) {
																																// No es campo de formulario, guardamos el fichero en algún sitio
																																File directory = new File("webapps/TU-Charts-Server/tmp");
																																if (!directory.exists())
																																	directory.mkdir();
																																fichero = new File(directory,(rand.nextInt(1000 - 100) + 100)+ uploaded.getName());
																																uploaded.write(fichero);
																															} else
																																map.put(uploaded.getFieldName(), uploaded.getString());

																														}

																														String name = map.get("name");
																														String description = map.get("description");
																														String xLegend = map.get("xLegend");
																														String yLegend = map.get("yLegend");
																														String type = map.get("type");
																														String category = map.get("category");
																														ChartModel c = ServerUtils.i.fromCSV(name, description, fichero);

																														CategoryModel cat = DB_Process.i.getCategory(category);
																														cat.addChart(c);
																														DB_Process.i.setChart(c);
															%><h4>
																<span style="color: green">New Chart Added!
															</h4>
															<%
																} else if (request.getParameter("delete") != null) {
																	int chart_id = Integer.parseInt(request.getParameter("delete"));
																	DB_Process.i.delChart(chart_id);
															%><h4>
																<span style="color: red">Chart Deleted!
															</h4>
															<%
																}
															%>

														</div>

													</div>
													<p>
														<br />
													</p>
													<h3>All Charts</h3>
													<%
														List<BaseChartModel> charts = DB_Process.i.getCharts();
													%>
													<br />


												</div>

											</div>
											<table id="rounded-corner"
												summary="2007 Major IT Companies' Profit" border="0"
												cellspacing="0" cellpadding="0" style="width: 100%;">

												<thead>

													<tr>

														<th scope="col" class="rounded-company">Name</th>
														<th scope="col" class="rounded-q2">Description</th>
														<th scope="col" class="rounded-q3">Category</th>
														<th scope="col" class="rounded-q3">Type</th>
														<th scope="col" class="rounded-q4">Manager</th>
													</tr>

												</thead>

												<tfoot>

													<tr>

														<td colspan="4" class="rounded-foot-left"><em>Charts data obtained from the Database. The manager is able toModify or Delete charts. More info about the database structure <a href="out/">HERE</a></em></td>

														<td class="rounded-foot-right">&nbsp;</td>

													</tr>

												</tfoot>

												<tbody>
													<%
														for (BaseChartModel chart : charts) {
													%>
													<tr>
														<td><%=chart.getName()%></td>
														<td><%=chart.getDescription()%></td>
														<td><%=chart.getCategory().getName()%></td>
														<td><%=chart.getType()%></td>
														<td><a
															href="<%=request.getServletPath().replace("/", "") + "?edit="+ chart.getId()%>" /><img
															src="images/edit.png" alt="Edit Chart" /></a> <a
															href="<%=request.getServletPath().replace("/", "") + "?delete=" + chart.getId()%>" /><img
															src="images/delete.png" alt="Delete Chart" /></a></td>
													</tr>

													<%
														}
													%>

												</tbody>
											</table>
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
		<p class="art-page-footer">
		</p>
		<div class="cleared"></div>
	</div>

</body>
</html>
