package rest.resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
@Produces("application/json")
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	private JAXBContext context;
	private final Set<Class> types;
	private final Class[] cTypes = {
			ChartsResource.class,
			ChartRes.class,
			ChartCommentRes.class,
			UserRes.class,
			UsersResource.class,
			CategoriesResource.class,
			CategoryRes.class
	};

	public JAXBContextResolver () throws Exception {
		this.types = new HashSet(Arrays.asList(cTypes));
		this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), cTypes);
	}

	public JAXBContext getContext(Class<?> objectType) {
		return (types.contains(objectType)) ? context : null;
	}
}
