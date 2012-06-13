//package rest.resources;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Type;
//
//import javax.ws.rs.Produces;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.ext.MessageBodyWriter;
//import javax.ws.rs.ext.Provider;
//
//import org.codehaus.jackson.map.ObjectMapper;
//
//@Provider
//@Produces("application/json")
//public class JsonMessageBodyWriter<T> implements MessageBodyWriter<T> {
//
//	@Override
//	public long getSize(T arg0, Class<?> arg1, Type arg2, Annotation[] arg3,
//			MediaType arg4) {
//		return 0;
//	}
//
//	@Override
//	public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2,
//			MediaType arg3) {
//		return true;
//	}
//
//	@Override
//	public void writeTo(T target, Class<?> arg1, Type arg2, Annotation[] arg3,
//			MediaType arg4, MultivaluedMap<String, Object> arg5,
//			OutputStream outputStream) throws IOException, WebApplicationException {
//		new ObjectMapper().writeValue(outputStream, target);
//		
//	}
//
//}
