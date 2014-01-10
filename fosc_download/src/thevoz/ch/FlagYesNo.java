
package thevoz.ch;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for flagYesNo.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="flagYesNo">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FLAG_YES"/>
 *     &lt;enumeration value="FLAG_NO"/>
 *     &lt;enumeration value="UNDEFINIED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "flagYesNo")
@XmlEnum
public enum FlagYesNo {

    FLAG_YES,
    FLAG_NO,
    UNDEFINIED;

    public String value() {
        return name();
    }

    public static FlagYesNo fromValue(String v) {
        return valueOf(v);
    }

}
