package thevoz.ch;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

public class ServiceHelper {
	public static SoapServer getService(String username, String password) throws MalformedURLException, Fault {
		String servername = "www.shab.ch";
		int port = 9191;
		
		URL url = new URL("http://" + servername + ":" + port
				+ "/soapserver?wsdl");
		QName serviceName = new QName(
				"http://notice.server.soap.common.exchange.autinform.de/",
				"SoapServerService");
		SoapServerService service = new SoapServerService(url, serviceName);
		SoapServer servicePort = service.getSoapServerPort();
		/******************* Authentication ******************************/
		Map<String, Object> req_ctx = ((BindingProvider) servicePort)
				.getRequestContext();
		Map<String, List<String>> headers = new HashMap<String, List<String>>();

		headers.put("username", Collections.singletonList(username));
		headers.put("password", Collections.singletonList(password));
		req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
		/**********************************************************************/
		boolean succesfulAuthentication = servicePort.getAuthentication();
		if (succesfulAuthentication) {
			System.out.println("Authentication successful!");
		} else {
			System.out.println("Authentication not successful!");
			return null;
		}
		return servicePort;
	}
}
