//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2018.08.30 at 11:25:19 AM BST
//

package net.sf.mpxj.mspdi.schema;

import java.math.BigInteger;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The definition of the time phased data block.
 *
 * <p>Java class for TimephasedDataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TimephasedDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="4"/>
 *               &lt;enumeration value="5"/>
 *               &lt;enumeration value="6"/>
 *               &lt;enumeration value="7"/>
 *               &lt;enumeration value="8"/>
 *               &lt;enumeration value="9"/>
 *               &lt;enumeration value="10"/>
 *               &lt;enumeration value="11"/>
 *               &lt;enumeration value="16"/>
 *               &lt;enumeration value="17"/>
 *               &lt;enumeration value="18"/>
 *               &lt;enumeration value="19"/>
 *               &lt;enumeration value="20"/>
 *               &lt;enumeration value="21"/>
 *               &lt;enumeration value="22"/>
 *               &lt;enumeration value="23"/>
 *               &lt;enumeration value="24"/>
 *               &lt;enumeration value="25"/>
 *               &lt;enumeration value="26"/>
 *               &lt;enumeration value="27"/>
 *               &lt;enumeration value="28"/>
 *               &lt;enumeration value="29"/>
 *               &lt;enumeration value="30"/>
 *               &lt;enumeration value="31"/>
 *               &lt;enumeration value="32"/>
 *               &lt;enumeration value="33"/>
 *               &lt;enumeration value="34"/>
 *               &lt;enumeration value="35"/>
 *               &lt;enumeration value="36"/>
 *               &lt;enumeration value="37"/>
 *               &lt;enumeration value="38"/>
 *               &lt;enumeration value="39"/>
 *               &lt;enumeration value="40"/>
 *               &lt;enumeration value="41"/>
 *               &lt;enumeration value="42"/>
 *               &lt;enumeration value="43"/>
 *               &lt;enumeration value="44"/>
 *               &lt;enumeration value="45"/>
 *               &lt;enumeration value="46"/>
 *               &lt;enumeration value="47"/>
 *               &lt;enumeration value="48"/>
 *               &lt;enumeration value="49"/>
 *               &lt;enumeration value="50"/>
 *               &lt;enumeration value="51"/>
 *               &lt;enumeration value="52"/>
 *               &lt;enumeration value="53"/>
 *               &lt;enumeration value="54"/>
 *               &lt;enumeration value="55"/>
 *               &lt;enumeration value="56"/>
 *               &lt;enumeration value="57"/>
 *               &lt;enumeration value="58"/>
 *               &lt;enumeration value="59"/>
 *               &lt;enumeration value="60"/>
 *               &lt;enumeration value="61"/>
 *               &lt;enumeration value="62"/>
 *               &lt;enumeration value="63"/>
 *               &lt;enumeration value="64"/>
 *               &lt;enumeration value="65"/>
 *               &lt;enumeration value="66"/>
 *               &lt;enumeration value="67"/>
 *               &lt;enumeration value="68"/>
 *               &lt;enumeration value="69"/>
 *               &lt;enumeration value="70"/>
 *               &lt;enumeration value="71"/>
 *               &lt;enumeration value="72"/>
 *               &lt;enumeration value="73"/>
 *               &lt;enumeration value="74"/>
 *               &lt;enumeration value="75"/>
 *               &lt;enumeration value="76"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="UID" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Start" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Finish" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="Unit" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *               &lt;enumeration value="2"/>
 *               &lt;enumeration value="3"/>
 *               &lt;enumeration value="5"/>
 *               &lt;enumeration value="8"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@SuppressWarnings("all") @XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "TimephasedDataType", propOrder =
{
   "type",
   "uid",
   "start",
   "finish",
   "unit",
   "value"
}) public class TimephasedDataType
{

   @XmlElement(name = "Type") protected BigInteger type;
   @XmlElement(name = "UID", required = true) protected BigInteger uid;
   @XmlElement(name = "Start", type = String.class) @XmlJavaTypeAdapter(Adapter1.class) @XmlSchemaType(name = "dateTime") protected Calendar start;
   @XmlElement(name = "Finish", type = String.class) @XmlJavaTypeAdapter(Adapter1.class) @XmlSchemaType(name = "dateTime") protected Calendar finish;
   @XmlElement(name = "Unit") protected BigInteger unit;
   @XmlElement(name = "Value") protected String value;

   /**
    * Gets the value of the type property.
    *
    * @return
    *     possible object is
    *     {@link BigInteger }
    *
    */
   public BigInteger getType()
   {
      return type;
   }

   /**
    * Sets the value of the type property.
    *
    * @param value
    *     allowed object is
    *     {@link BigInteger }
    *
    */
   public void setType(BigInteger value)
   {
      this.type = value;
   }

   /**
    * Gets the value of the uid property.
    *
    * @return
    *     possible object is
    *     {@link BigInteger }
    *
    */
   public BigInteger getUID()
   {
      return uid;
   }

   /**
    * Sets the value of the uid property.
    *
    * @param value
    *     allowed object is
    *     {@link BigInteger }
    *
    */
   public void setUID(BigInteger value)
   {
      this.uid = value;
   }

   /**
    * Gets the value of the start property.
    *
    * @return
    *     possible object is
    *     {@link String }
    *
    */
   public Calendar getStart()
   {
      return start;
   }

   /**
    * Sets the value of the start property.
    *
    * @param value
    *     allowed object is
    *     {@link String }
    *
    */
   public void setStart(Calendar value)
   {
      this.start = value;
   }

   /**
    * Gets the value of the finish property.
    *
    * @return
    *     possible object is
    *     {@link String }
    *
    */
   public Calendar getFinish()
   {
      return finish;
   }

   /**
    * Sets the value of the finish property.
    *
    * @param value
    *     allowed object is
    *     {@link String }
    *
    */
   public void setFinish(Calendar value)
   {
      this.finish = value;
   }

   /**
    * Gets the value of the unit property.
    *
    * @return
    *     possible object is
    *     {@link BigInteger }
    *
    */
   public BigInteger getUnit()
   {
      return unit;
   }

   /**
    * Sets the value of the unit property.
    *
    * @param value
    *     allowed object is
    *     {@link BigInteger }
    *
    */
   public void setUnit(BigInteger value)
   {
      this.unit = value;
   }

   /**
    * Gets the value of the value property.
    *
    * @return
    *     possible object is
    *     {@link String }
    *
    */
   public String getValue()
   {
      return value;
   }

   /**
    * Sets the value of the value property.
    *
    * @param value
    *     allowed object is
    *     {@link String }
    *
    */
   public void setValue(String value)
   {
      this.value = value;
   }

}
