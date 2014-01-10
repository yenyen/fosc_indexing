
package thevoz.ch;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptionDE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptionEN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptionFR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptionIT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="initDocument" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "type", propOrder = {
    "addType",
    "category",
    "descriptionDE",
    "descriptionEN",
    "descriptionFR",
    "descriptionIT",
    "initDocument",
    "type"
})
public class Type {

    protected String addType;
    protected String category;
    protected String descriptionDE;
    protected String descriptionEN;
    protected String descriptionFR;
    protected String descriptionIT;
    protected String initDocument;
    protected String type;

    /**
     * Gets the value of the addType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddType() {
        return addType;
    }

    /**
     * Sets the value of the addType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddType(String value) {
        this.addType = value;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the descriptionDE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionDE() {
        return descriptionDE;
    }

    /**
     * Sets the value of the descriptionDE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionDE(String value) {
        this.descriptionDE = value;
    }

    /**
     * Gets the value of the descriptionEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionEN() {
        return descriptionEN;
    }

    /**
     * Sets the value of the descriptionEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionEN(String value) {
        this.descriptionEN = value;
    }

    /**
     * Gets the value of the descriptionFR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionFR() {
        return descriptionFR;
    }

    /**
     * Sets the value of the descriptionFR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionFR(String value) {
        this.descriptionFR = value;
    }

    /**
     * Gets the value of the descriptionIT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionIT() {
        return descriptionIT;
    }

    /**
     * Sets the value of the descriptionIT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionIT(String value) {
        this.descriptionIT = value;
    }

    /**
     * Gets the value of the initDocument property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInitDocument() {
        return initDocument;
    }

    /**
     * Sets the value of the initDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInitDocument(String value) {
        this.initDocument = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
