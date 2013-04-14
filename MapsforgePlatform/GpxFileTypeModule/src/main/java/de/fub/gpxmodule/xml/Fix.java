//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.03.14 at 08:42:30 PM MEZ
//
package de.fub.gpxmodule.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for fixType.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fixType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="none"/>
 *     &lt;enumeration value="2d"/>
 *     &lt;enumeration value="3d"/>
 *     &lt;enumeration value="dgps"/>
 *     &lt;enumeration value="pps"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "fixType")
@XmlEnum
public enum Fix {

    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("2d")
    TWO_D("2d"),
    @XmlEnumValue("3d")
    THREE_D("3d"),
    @XmlEnumValue("dgps")
    DGPS("dgps"),
    @XmlEnumValue("pps")
    PPS("pps");
    private final String value;

    Fix(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Fix fromValue(String v) {
        for (Fix c : Fix.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
