package heigvd.bda.labs.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="FORM")
public class FormModel {
	@XmlAttribute(name="ID")
	public Long id;

	@XmlAttribute(name="FOSC_NR")
	public Long number;

	@XmlAttribute(name="USER")
	public Long userId;

	@XmlAttribute(name="LANG")
	public String language;

	@XmlAttribute(name="VERSION")
	public String version;
	
	@XmlElements({
		@XmlElement(name="AN01", type=An01.class),
		@XmlElement(name="AN02", type=An02.class),
		@XmlElement(name="AN08", type=An08.class),
		@XmlElement(name="AN17", type=An17.class),
		@XmlElement(name="AN21", type=An21.class),
		
		@XmlElement(name="AW01", type=Aw01.class),
		@XmlElement(name="AW02", type=Aw02.class),
		@XmlElement(name="AW08", type=Aw08.class),
		@XmlElement(name="AW17", type=Aw17.class),
		@XmlElement(name="AW21", type=Aw21.class),

	})
	public List<AbstractAnnotationModel> annotations;

	@XmlType(name="AN01")
	public static class An01 extends AbstractAnnotationModel{}
	@XmlType(name="AN02")
	public static class An02 extends AbstractAnnotationModel{}
	@XmlType(name="AN08")
	public static class An08 extends AbstractAnnotationModel{}
	@XmlType(name="AN17")
	public static class An17 extends AbstractAnnotationModel{}
	@XmlType(name="AN21")
	public static class An21 extends AbstractAnnotationModel{}

	@XmlType(name="AW01")
	public static class Aw01 extends AbstractAnnotationModel{}
	@XmlType(name="AW02")
	public static class Aw02 extends AbstractAnnotationModel{}
	@XmlType(name="AW08")
	public static class Aw08 extends AbstractAnnotationModel{}
	@XmlType(name="AW17")
	public static class Aw17 extends AbstractAnnotationModel{}
	@XmlType(name="AW21")
	public static class Aw21 extends AbstractAnnotationModel{}
	

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName()).append("[");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (number != null) {
			builder.append("number=");
			builder.append(number);
			builder.append(", ");
		}
		if (userId != null) {
			builder.append("userId=");
			builder.append(userId);
			builder.append(", ");
		}
		if (language != null) {
			builder.append("language=");
			builder.append(language);
			builder.append(", ");
		}
		if (version != null) {
			builder.append("version=");
			builder.append(version);
			builder.append(", ");
		}
		if (annotations != null) {
			builder.append("annotations=");
			builder.append(annotations);
		}
		builder.append("]");
		return builder.toString();
	}
	public static FormModel parse(String xml) {
		return parse(new StringReader(xml));
	}
	public static FormModel parse(Reader xml) {
		FormModel r = null;
		r = javax.xml.bind.JAXB.unmarshal(xml, FormModel.class);
		return r;
	}

	public static void main(String[] args) throws Exception {

		java.io.BufferedReader br = new BufferedReader(new FileReader(
				"data/out.txt"));
		//Pattern p = Pattern.compile("([0-9]+)\\\\s+(.+)", Pattern.MULTILINE);
		try {
			Long id=null;
			for (String s = br.readLine(); s != null; s = br.readLine()) {
				int i=s.indexOf("<?");
				if(i<0)
					continue;

				id=Long.parseLong(s.substring(0, i-1).trim());
				FormModel o = parse(s.substring(i).trim());
				System.out.println(id);
				System.out.println(o);
				//Matcher m = p.matcher(s);
				
				//System.out.println(m.group(1));
				//System.out.println(m.group(2));
			}
		} finally {
			br.close();
		}
	}
}
