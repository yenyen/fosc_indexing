package thevoz.ch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import thevoz.ch.Fault;
import thevoz.ch.LongArray;
import thevoz.ch.SoapServer;
import thevoz.ch.SoapServerService;

public class Main {
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd.MM.yyyy", Locale.getDefault());

	public Main() {

	}

	public static void main(String[] args) {
		// Erstellung einer Verbindung zum Soap Server
		String username = args[0];
		String password = args[1];
		try {
			SoapServer servicePort = ServiceHelper.getService(username, password);
			if(servicePort != null)
			{
				// Create file 
				FileWriter fstream = new FileWriter("out.txt");
				BufferedWriter out = new BufferedWriter(fstream);
	
				GregorianCalendar c = new GregorianCalendar();
				c.setTime(dateFormat.parse("14.11.2013"));
				XMLGregorianCalendar publishDate;
				
				for(int j = 0; j < 365; j++) {
					publishDate = DatatypeFactory.newInstance()
							.newXMLGregorianCalendar(c);
					LongArray arrayList = servicePort.getPublishedNoticeList(publishDate);
					List<Long> noticeList = arrayList.getItem();
		
					for (int i = 0; i < noticeList.size(); i++) {
						long documentId = noticeList.get(i);
						String contentXml = servicePort.getNoticeXml(documentId);
						out.write(documentId + " " + contentXml.replace("\n", " ")+ "\n");
					}
					System.out.println(c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR));
					c.add(Calendar.DAY_OF_MONTH, 1);
				}
				out.close();
			}
			System.out.println("FIN");
		} catch (Fault f) {
			 System.out.println("Fault Code:" + f.getFaultInfo().getCode()); 
			 System.out.println("Message:" + f.getFaultInfo().getMessage()); 
			 f.printStackTrace();
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
