//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2017.07.12 at 07:27:33 PM BST
//

package net.sf.mpxj.phoenix.schema;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import net.sf.mpxj.Duration;

public class Adapter9 extends XmlAdapter<String, Duration>
{

   @Override public Duration unmarshal(String value)
   {
      return (net.sf.mpxj.phoenix.DatatypeConverter.parseDuration(value));
   }

   @Override public String marshal(Duration value)
   {
      return (net.sf.mpxj.phoenix.DatatypeConverter.printDuration(value));
   }

}
