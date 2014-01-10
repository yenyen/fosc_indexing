
package thevoz.ch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for userProfile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="userProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addressSupplement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authDemand" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authorityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="canton" type="{http://notice.server.soap.common.exchange.autinform.de/}canton" minOccurs="0"/>
 *         &lt;element name="cantonCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cantonExport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country" type="{http://notice.server.soap.common.exchange.autinform.de/}country" minOccurs="0"/>
 *         &lt;element name="countryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="federalInstitution" type="{http://notice.server.soap.common.exchange.autinform.de/}federalInstitution" minOccurs="0"/>
 *         &lt;element name="federalInstitutionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="municipality" type="{http://notice.server.soap.common.exchange.autinform.de/}municipality" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://notice.server.soap.common.exchange.autinform.de/}authorityType" minOccurs="0"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zipCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userProfile", propOrder = {
    "address",
    "addressSupplement",
    "authDemand",
    "authorityType",
    "canton",
    "cantonCode",
    "cantonExport",
    "city",
    "cityCode",
    "country",
    "countryCode",
    "fax",
    "federalInstitution",
    "federalInstitutionId",
    "mail",
    "municipality",
    "name",
    "phone",
    "type",
    "url",
    "zipCode"
})
public class UserProfile {

    protected String address;
    protected String addressSupplement;
    protected String authDemand;
    protected String authorityType;
    protected Canton canton;
    protected String cantonCode;
    protected String cantonExport;
    protected String city;
    protected String cityCode;
    protected Country country;
    protected String countryCode;
    protected String fax;
    protected FederalInstitution federalInstitution;
    protected String federalInstitutionId;
    protected String mail;
    protected Municipality municipality;
    protected String name;
    protected String phone;
    protected AuthorityType type;
    protected String url;
    protected String zipCode;

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the addressSupplement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressSupplement() {
        return addressSupplement;
    }

    /**
     * Sets the value of the addressSupplement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressSupplement(String value) {
        this.addressSupplement = value;
    }

    /**
     * Gets the value of the authDemand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthDemand() {
        return authDemand;
    }

    /**
     * Sets the value of the authDemand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthDemand(String value) {
        this.authDemand = value;
    }

    /**
     * Gets the value of the authorityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorityType() {
        return authorityType;
    }

    /**
     * Sets the value of the authorityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorityType(String value) {
        this.authorityType = value;
    }

    /**
     * Gets the value of the canton property.
     * 
     * @return
     *     possible object is
     *     {@link Canton }
     *     
     */
    public Canton getCanton() {
        return canton;
    }

    /**
     * Sets the value of the canton property.
     * 
     * @param value
     *     allowed object is
     *     {@link Canton }
     *     
     */
    public void setCanton(Canton value) {
        this.canton = value;
    }

    /**
     * Gets the value of the cantonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCantonCode() {
        return cantonCode;
    }

    /**
     * Sets the value of the cantonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCantonCode(String value) {
        this.cantonCode = value;
    }

    /**
     * Gets the value of the cantonExport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCantonExport() {
        return cantonExport;
    }

    /**
     * Sets the value of the cantonExport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCantonExport(String value) {
        this.cantonExport = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the cityCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * Sets the value of the cityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityCode(String value) {
        this.cityCode = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link Country }
     *     
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link Country }
     *     
     */
    public void setCountry(Country value) {
        this.country = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * Gets the value of the federalInstitution property.
     * 
     * @return
     *     possible object is
     *     {@link FederalInstitution }
     *     
     */
    public FederalInstitution getFederalInstitution() {
        return federalInstitution;
    }

    /**
     * Sets the value of the federalInstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link FederalInstitution }
     *     
     */
    public void setFederalInstitution(FederalInstitution value) {
        this.federalInstitution = value;
    }

    /**
     * Gets the value of the federalInstitutionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFederalInstitutionId() {
        return federalInstitutionId;
    }

    /**
     * Sets the value of the federalInstitutionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFederalInstitutionId(String value) {
        this.federalInstitutionId = value;
    }

    /**
     * Gets the value of the mail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the value of the mail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMail(String value) {
        this.mail = value;
    }

    /**
     * Gets the value of the municipality property.
     * 
     * @return
     *     possible object is
     *     {@link Municipality }
     *     
     */
    public Municipality getMunicipality() {
        return municipality;
    }

    /**
     * Sets the value of the municipality property.
     * 
     * @param value
     *     allowed object is
     *     {@link Municipality }
     *     
     */
    public void setMunicipality(Municipality value) {
        this.municipality = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the phone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the value of the phone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorityType }
     *     
     */
    public AuthorityType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorityType }
     *     
     */
    public void setType(AuthorityType value) {
        this.type = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the zipCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the value of the zipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZipCode(String value) {
        this.zipCode = value;
    }

}
