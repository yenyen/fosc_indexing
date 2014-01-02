package heigvd.bda.labs.model;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

public abstract class AbstractAnnotationModel {
	@XmlElement(name="NOTICE.REF")
	public String noticeRef;
	@XmlElement(name="PUB.DATE")
	public String publicationDate;
	@XmlElement(name="SUBMITION")
	public Submition submition;
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName()).append("[");
		if (noticeRef != null) {
			builder.append("noticeRef=");
			builder.append(noticeRef);
			builder.append(", ");
		}
		if (publicationDate != null) {
			builder.append("publicationDate=");
			builder.append(publicationDate);
			builder.append(", ");
		}
		if (submition != null) {
			builder.append("submition=");
			builder.append(submition);
		}
		builder.append("]");
		return builder.toString();
	}


	@XmlType(name="SUBMITION")
	public static class Submition{
		@XmlElement(name="ZIPCODE")
		public String zipCode;
		@XmlElement(name="CITY")
		public String city;
		@XmlElement(name="SUBMITOR")
		public String submitor;
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(getClass().getSimpleName()).append("[");
			if (zipCode != null) {
				builder.append("zipCode=");
				builder.append(zipCode);
				builder.append(", ");
			}
			if (city != null) {
				builder.append("city=");
				builder.append(city);
				builder.append(", ");
			}
			if (submitor != null) {
				builder.append("submitor=");
				builder.append(submitor);
			}
			builder.append("]");
			return builder.toString();
		}
		
		
	}
}
