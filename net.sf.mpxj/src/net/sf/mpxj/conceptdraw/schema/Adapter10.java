//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2018.07.12 at 04:42:45 PM BST
//

package net.sf.mpxj.conceptdraw.schema;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter10 extends XmlAdapter<String, Double>
{

   @Override public Double unmarshal(String value)
   {
      return (net.sf.mpxj.conceptdraw.DatatypeConverter.parsePercent(value));
   }

   @Override public String marshal(Double value)
   {
      return (net.sf.mpxj.conceptdraw.DatatypeConverter.printPercent(value));
   }

}
