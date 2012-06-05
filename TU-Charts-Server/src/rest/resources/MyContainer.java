package rest.resources;

import hibernate.db.SessionFactoryHibernate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.WebApplication;
import com.sun.jersey.spi.container.servlet.ServletContainer;


public class MyContainer extends ServletContainer{

	@Override
	protected void initiate(ResourceConfig rc, WebApplication wa) {
		super.initiate(rc, wa);
		SessionFactoryHibernate.getSingleton();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.service(request, response);
	}

}
