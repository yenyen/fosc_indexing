
package thevoz.ch;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for category complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="category">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="catCD" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="internal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="typeList" type="{http://notice.server.soap.common.exchange.autinform.de/}type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "category", propOrder = {
    "catCD",
    "de",
    "en",
    "fr",
    "it",
    "internal",
    "order",
    "typeList"
})
public class Category {

    protected String catCD;
    @XmlElement(name = "DE")
    protected String de;
    @XmlElement(name = "EN")
    protected String en;
    @XmlElement(name = "FR")
    protected String fr;
    @XmlElement(name = "IT")
    protected String it;
    protected String internal;
    protected Integer order;
    @XmlElement(nillable = true)
    protected List<Type> typeList;

    /**
     * Gets the value of the catCD property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCatCD() {
        return catCD;
    }

    /**
     * Sets the value of the catCD property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCatCD(String value) {
        this.catCD = value;
    }

    /**
     * Gets the value of the de property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDE() {
        return de;
    }

    /**
     * Sets the value of the de property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDE(String value) {
        this.de = value;
    }

    /**
     * Gets the value of the en property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEN() {
        return en;
    }

    /**
     * Sets the value of the en property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEN(String value) {
        this.en = value;
    }

    /**
     * Gets the value of the fr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFR() {
        return fr;
    }

    /**
     * Sets the value of the fr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFR(String value) {
        this.fr = value;
    }

    /**
     * Gets the value of the it property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIT() {
        return it;
    }

    /**
     * Sets the value of the it property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIT(String value) {
        this.it = value;
    }

    /**
     * Gets the value of the internal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternal() {
        return internal;
    }

    /**
     * Sets the value of the internal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternal(String value) {
        this.internal = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrder(Integer value) {
        this.order = value;
    }

    /**
     * Gets the value of the typeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the typeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTypeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Type }
     * 
     * 
     */
    public List<Type> getTypeList() {
        if (typeList == null) {
            typeList = new ArrayList<Type>();
        }
        return this.typeList;
    }

}
