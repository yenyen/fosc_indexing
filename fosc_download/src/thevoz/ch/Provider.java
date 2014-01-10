
package thevoz.ch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for provider complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="provider">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="authentificationProfile" type="{http://notice.server.soap.common.exchange.autinform.de/}authentificationProfile" minOccurs="0"/>
 *         &lt;element name="contactProfile" type="{http://notice.server.soap.common.exchange.autinform.de/}contactProfile" minOccurs="0"/>
 *         &lt;element name="creation" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lastupdated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastupdatedFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="providerComment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userProfile" type="{http://notice.server.soap.common.exchange.autinform.de/}userProfile" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "provider", propOrder = {
    "authentificationProfile",
    "contactProfile",
    "creation",
    "id",
    "lastupdated",
    "lastupdatedFrom",
    "providerComment",
    "userProfile",
    "version"
})
public class Provider {

    protected AuthentificationProfile authentificationProfile;
    protected ContactProfile contactProfile;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creation;
    protected int id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastupdated;
    protected String lastupdatedFrom;
    protected String providerComment;
    protected UserProfile userProfile;
    protected int version;

    /**
     * Gets the value of the authentificationProfile property.
     * 
     * @return
     *     possible object is
     *     {@link AuthentificationProfile }
     *     
     */
    public AuthentificationProfile getAuthentificationProfile() {
        return authentificationProfile;
    }

    /**
     * Sets the value of the authentificationProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthentificationProfile }
     *     
     */
    public void setAuthentificationProfile(AuthentificationProfile value) {
        this.authentificationProfile = value;
    }

    /**
     * Gets the value of the contactProfile property.
     * 
     * @return
     *     possible object is
     *     {@link ContactProfile }
     *     
     */
    public ContactProfile getContactProfile() {
        return contactProfile;
    }

    /**
     * Sets the value of the contactProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactProfile }
     *     
     */
    public void setContactProfile(ContactProfile value) {
        this.contactProfile = value;
    }

    /**
     * Gets the value of the creation property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreation() {
        return creation;
    }

    /**
     * Sets the value of the creation property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreation(XMLGregorianCalendar value) {
        this.creation = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the lastupdated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastupdated() {
        return lastupdated;
    }

    /**
     * Sets the value of the lastupdated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastupdated(XMLGregorianCalendar value) {
        this.lastupdated = value;
    }

    /**
     * Gets the value of the lastupdatedFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastupdatedFrom() {
        return lastupdatedFrom;
    }

    /**
     * Sets the value of the lastupdatedFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastupdatedFrom(String value) {
        this.lastupdatedFrom = value;
    }

    /**
     * Gets the value of the providerComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProviderComment() {
        return providerComment;
    }

    /**
     * Sets the value of the providerComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProviderComment(String value) {
        this.providerComment = value;
    }

    /**
     * Gets the value of the userProfile property.
     * 
     * @return
     *     possible object is
     *     {@link UserProfile }
     *     
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * Sets the value of the userProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserProfile }
     *     
     */
    public void setUserProfile(UserProfile value) {
        this.userProfile = value;
    }

    /**
     * Gets the value of the version property.
     * 
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     */
    public void setVersion(int value) {
        this.version = value;
    }

}
