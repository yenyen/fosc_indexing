
package thevoz.ch;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the thevoz.ch package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Fault_QNAME = new QName("http://www.kps-consulting.com", "Fault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: thevoz.ch
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FaultDetail }
     * 
     */
    public FaultDetail createFaultDetail() {
        return new FaultDetail();
    }

    /**
     * Create an instance of {@link UserProfile }
     * 
     */
    public UserProfile createUserProfile() {
        return new UserProfile();
    }

    /**
     * Create an instance of {@link Municipality }
     * 
     */
    public Municipality createMunicipality() {
        return new Municipality();
    }

    /**
     * Create an instance of {@link Provider }
     * 
     */
    public Provider createProvider() {
        return new Provider();
    }

    /**
     * Create an instance of {@link Canton }
     * 
     */
    public Canton createCanton() {
        return new Canton();
    }

    /**
     * Create an instance of {@link Type }
     * 
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link AuthorityType }
     * 
     */
    public AuthorityType createAuthorityType() {
        return new AuthorityType();
    }

    /**
     * Create an instance of {@link ContactProfile }
     * 
     */
    public ContactProfile createContactProfile() {
        return new ContactProfile();
    }

    /**
     * Create an instance of {@link Country }
     * 
     */
    public Country createCountry() {
        return new Country();
    }

    /**
     * Create an instance of {@link Category }
     * 
     */
    public Category createCategory() {
        return new Category();
    }

    /**
     * Create an instance of {@link AuthentificationProfile }
     * 
     */
    public AuthentificationProfile createAuthentificationProfile() {
        return new AuthentificationProfile();
    }

    /**
     * Create an instance of {@link FederalInstitution }
     * 
     */
    public FederalInstitution createFederalInstitution() {
        return new FederalInstitution();
    }

    /**
     * Create an instance of {@link Language }
     * 
     */
    public Language createLanguage() {
        return new Language();
    }

    /**
     * Create an instance of {@link LongArray }
     * 
     */
    public LongArray createLongArray() {
        return new LongArray();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultDetail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.kps-consulting.com", name = "Fault")
    public JAXBElement<FaultDetail> createFault(FaultDetail value) {
        return new JAXBElement<FaultDetail>(_Fault_QNAME, FaultDetail.class, null, value);
    }

}
