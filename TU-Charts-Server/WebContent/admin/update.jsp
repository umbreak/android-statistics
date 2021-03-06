<%@page import="javax.annotation.processing.Processor"%>
<%@page import="utils.ServerUtils"%>
<%@page import="hibernate.db.DB_Process"%>
<%@page import="models.BaseChartModel"%>
<%@page import="models.ChartModel"%>
<%@page import="java.util.List"%>
<%@page import="models.BaseChartModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="models.CategoryModel"%>
<%@page import="hibernate.db.DB_Process"%>
<%@page import="models.ChartModel"%>
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
	String id=request.getParameter("edit");
   	 if (id == null){
   		response.sendRedirect("index.jsp");
   	 }else{
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
																														// Los items obtenidos ser�n cada uno de los campos del formulario,
																														// tanto campos normales como ficheros subidos.

																														List<FileItem> items = upload.parseRequest(request);
																														File fichero = null;
																														// Se recorren todos los items, que son de tipo FileItem
																														for (Object item : items) {
																															FileItem uploaded = (FileItem) item;

																															// Hay que comprobar si es un campo de formulario. Si no lo es, se guarda el fichero
																															// subido donde nos interese
																															if (!uploaded.isFormField()) {
																																// No es campo de formulario, guardamos el fichero en alg�n sitio
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
																														ChartModel newChart = ServerUtils.i.fromCSV(name, description, fichero);
																														DB_Process.i.uploadChart(Integer.parseInt(id), newChart);
															%><h4>
																<span style="color: green">Chart Updated!
															</h4>
															<%
															response.sendRedirect("index.jsp?update="+id);
															} else{
																ChartModel chart = DB_Process.i.getChart(Integer.valueOf(id)); 
															%>
															<div class="art-layout-cell layout-item-2"
															style="width: 65%;">
															<h3>Update Chart</h3>

															<p>
																<br />
															</p>

															<form enctype="multipart/form-data" method="post"
																action="update.jsp?edit=<%= id %>">
																<table border="0" cellspacing="0" cellpadding="0"
																	style="width: 100%; margin-top: 2px; margin-bottom: 2px; margin-left: 2px; margin-right: 2px;">
																	<tbody>
																		<tr>
																			<td width="29%">Name:</td>

																			<td width="71%"><input name="name" type="text" value="<%=chart.getName()%>"
																				class="input" /></td>
																		</tr>

																		<tr>
																			<td>Description:</td>

																			<td><textarea name="description" class="input"><%=chart.getDescription()%></textarea></td>
																		</tr>
																		<tr>
																			<td>Load new values:</td>
																			<td><input type="file" name="data"></td>
																		</tr>
																		<tr>
																			<td colspan="2"><br /><input type="hidden" name="chart_id" value="<%=id%>" />
																			</td>
																		</tr>
																	</tbody>
																</table>
																<input class="art-button" type="Submit" value="Submit">
																<br />
																<br />

															</form>
														</div>
															<% }%>
														</div>

													</div>
													<p>
														<br />
													</p>

													<br />


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
								Copyright � 2012, TU-Charts.<br /> <br />
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
<%} %>
</body>
</html>
